package com.rent.healthbook.web.kafka;

import com.rent.healthbook.profile.dto.ProfileKafka;
import com.rent.healthbook.profile.service.ProfileService;
import com.rent.healthbook.web.kafka.dto.UserValidationRequest;
import com.rent.healthbook.web.kafka.dto.UserValidationResponse;
import com.rent.healthbook.web.kafka.dto.enums.ProfileAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationConsumerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Captor
    private ArgumentCaptor<UserValidationResponse> responseCaptor;

    private UserValidationConsumer consumer;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        consumer = new UserValidationConsumer(profileService, kafkaTemplate);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testHandleValidationRequest_GetProfile_ProfileFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String requestId = "req-123";
        UserValidationRequest request = new UserValidationRequest();
        request.setIdRequest(requestId);
        request.setIdUser(userId);
        request.setAction(ProfileAction.GET_PROFILE.name());

        String json = objectMapper.writeValueAsString(request);

        ProfileKafka mockProfile = new ProfileKafka(userId.toString(), "Dr. House", "DOCTOR");
        when(profileService.getByIdForKafka(userId)).thenReturn(mockProfile);

        // When
        consumer.handleValidationRequest(json);

        // Then
        verify(kafkaTemplate).send(eq("user-validation-responses"), responseCaptor.capture());
        UserValidationResponse response = responseCaptor.getValue();

        assertEquals(requestId, response.getIdRequest());
        assertTrue(response.isExists());
        assertSame(mockProfile, response.getProfile());
        assertNull(response.getErrorMessage());
    }

    @Test
    void testHandleValidationRequest_GetProfile_ProfileNotFound() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String requestId = "req-456";
        UserValidationRequest request = new UserValidationRequest();
        request.setIdRequest(requestId);
        request.setIdUser(userId);
        request.setAction(ProfileAction.GET_PROFILE.name());

        String json = objectMapper.writeValueAsString(request);

        when(profileService.getByIdForKafka(userId)).thenReturn(null);

        // When
        consumer.handleValidationRequest(json);

        // Then
        verify(kafkaTemplate).send(eq("user-validation-responses"), responseCaptor.capture());
        UserValidationResponse response = responseCaptor.getValue();

        assertEquals(requestId, response.getIdRequest());
        assertFalse(response.isExists());
        assertNull(response.getProfile());
        assertTrue(response.getErrorMessage().contains("не найден"));
    }

    @Test
    void testHandleValidationRequest_CheckExists_UserExists() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String requestId = "req-789";
        UserValidationRequest request = new UserValidationRequest();
        request.setIdRequest(requestId);
        request.setIdUser(userId);
        request.setAction(ProfileAction.CHECK_EXISTS.name());

        String json = objectMapper.writeValueAsString(request);

        when(profileService.existsByIdForKafka(userId)).thenReturn(true);

        // When
        consumer.handleValidationRequest(json);

        // Then
        verify(kafkaTemplate).send(eq("user-validation-responses"), responseCaptor.capture());
        UserValidationResponse response = responseCaptor.getValue();

        assertEquals(requestId, response.getIdRequest());
        assertTrue(response.isExists());
        assertNull(response.getProfile());
        assertNull(response.getErrorMessage());
    }

    @Test
    void testHandleValidationRequest_CheckExists_UserDoesNotExist() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String requestId = "req-101";
        UserValidationRequest request = new UserValidationRequest();
        request.setIdRequest(requestId);
        request.setIdUser(userId);
        request.setAction(ProfileAction.CHECK_EXISTS.name());

        String json = objectMapper.writeValueAsString(request);

        when(profileService.existsByIdForKafka(userId)).thenReturn(false);

        // When
        consumer.handleValidationRequest(json);

        // Then
        verify(kafkaTemplate).send(eq("user-validation-responses"), responseCaptor.capture());
        UserValidationResponse response = responseCaptor.getValue();

        assertFalse(response.isExists());
        assertNull(response.getProfile());
        assertNull(response.getErrorMessage());
    }

    @Test
    void testHandleValidationRequest_InvalidAction() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String requestId = "req-999";
        UserValidationRequest request = new UserValidationRequest();
        request.setIdRequest(requestId);
        request.setIdUser(userId);
        request.setAction("INVALID_ACTION");

        String json = objectMapper.writeValueAsString(request);

        // When
        consumer.handleValidationRequest(json);

        // Then
        verify(kafkaTemplate, never()).send(anyString(), any());
        verify(profileService, never()).getByIdForKafka(any());
        verify(profileService, never()).existsByIdForKafka(any());
    }

    @Test
    void testHandleValidationRequest_MalformedJson() {
        // Given
        String malformedJson = "{invalid json";

        consumer.handleValidationRequest(malformedJson);

        // Then
        verify(kafkaTemplate, never()).send(anyString(), any());

    }
}