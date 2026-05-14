package com.rent.healthbook.web.kafka;

import com.rent.healthbook.profile.dto.ProfileKafka;
import com.rent.healthbook.web.kafka.dto.ProfileEvent;
import com.rent.healthbook.web.kafka.dto.enums.ProfileEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Captor
    private ArgumentCaptor<ProfileEvent> eventCaptor;

    private ProfileEventProducer producer;

    @BeforeEach
    void setUp() {
        producer = new ProfileEventProducer(kafkaTemplate);
    }

    @Test
    void testSendProfileCreated() {
        // Given
        ProfileKafka profile = new ProfileKafka("123e4567-e89b-12d3-a456-426614174000", "John Doe", "DOCTOR");

        // When
        producer.sendProfileCreated(profile);

        // Then
        verify(kafkaTemplate).send(eq("profile-events"), eventCaptor.capture());
        ProfileEvent captured = eventCaptor.getValue();

        assertEquals(ProfileEventType.PROFILE_CREATED.name(), captured.getEventType());
        assertEquals(UUID.fromString(profile.getId()), captured.getProfileId());
        assertEquals(profile.getName(), captured.getName());
        assertEquals(profile.getRole(), captured.getRole());
        assertNotNull(captured.getTimestamp());
    }

    @Test
    void testSendProfileUpdated() {
        // Given
        ProfileKafka profile = new ProfileKafka("550e8400-e29b-41d4-a716-446655440000", "Jane Smith", "CONSULTANT");

        // When
        producer.sendProfileUpdated(profile);

        // Then
        verify(kafkaTemplate).send(eq("profile-events"), eventCaptor.capture());
        ProfileEvent captured = eventCaptor.getValue();

        assertEquals(ProfileEventType.PROFILE_UPDATED.name(), captured.getEventType());
        assertEquals(UUID.fromString(profile.getId()), captured.getProfileId());
        assertEquals(profile.getName(), captured.getName());
        assertEquals(profile.getRole(), captured.getRole());
        assertNotNull(captured.getTimestamp());
    }

    @Test
    void testSendProfileDeleted() {
        // Given
        UUID idProfile = UUID.randomUUID();

        // When
        producer.sendProfileDeleted(idProfile);

        // Then
        verify(kafkaTemplate).send(eq("profile-events"), eventCaptor.capture());
        ProfileEvent captured = eventCaptor.getValue();

        assertEquals(ProfileEventType.PROFILE_DELETED.name(), captured.getEventType());
        assertEquals(idProfile, captured.getProfileId());
        assertNull(captured.getName());
        assertNull(captured.getRole());
        assertNotNull(captured.getTimestamp());
    }
}