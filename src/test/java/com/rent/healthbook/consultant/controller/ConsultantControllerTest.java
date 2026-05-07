package com.rent.healthbook.consultant.controller;

import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.service.ConsultantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultantControllerTest {

    @Mock
    private ConsultantService consultantService;

    @InjectMocks
    private ConsultantController consultantController;

    @Test
    void addConsultant_ShouldReturnCreated() {
        // Create consultant using record constructor
        UUID id = UUID.randomUUID();
        Boolean online = true;
        UUID profileId = UUID.randomUUID();
        Consultant consultant = new Consultant(id, online, profileId);

        when(consultantService.addConsultant(any(Consultant.class)))
                .thenReturn(consultant);

        ResponseEntity<Consultant> response = consultantController.addConsultant(consultant);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        verify(consultantService, times(1)).addConsultant(consultant);
    }

    @Test
    void getConsultantById_ShouldReturnConsultant() {
        UUID id = UUID.randomUUID();
        Consultant consultant = new Consultant(id, true, UUID.randomUUID());

        when(consultantService.getById(id)).thenReturn(consultant);

        ResponseEntity<Consultant> response = consultantController.getConsultantById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(id, response.getBody().id());
        verify(consultantService, times(1)).getById(id);
    }

    @Test
    void deleteConsultant_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(consultantService).deleteConsultant(id);

        ResponseEntity<Void> response = consultantController.deleteConsultant(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(consultantService, times(1)).deleteConsultant(id);
    }

    @Test
    void updateConsultantOnline_ShouldReturnOk() {
        UUID id = UUID.randomUUID();
        Boolean online = true;
        doNothing().when(consultantService).updateConsultantOnline(id, online);

        ResponseEntity<Void> response = consultantController.updateConsultantOnline(id, online);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(consultantService, times(1)).updateConsultantOnline(id, online);
    }
    @Test
    void getConsultantById_WhenNotFound_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        when(consultantService.getById(nonExistentId))
                .thenThrow(new RuntimeException("Consultant not found"));

        assertThrows(RuntimeException.class, () -> consultantController.getConsultantById(nonExistentId));
    }

    @Test
    void deleteConsultant_WhenServiceThrowsException_ShouldPropagate() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Database error"))
                .when(consultantService).deleteConsultant(id);

        assertThrows(RuntimeException.class, () -> consultantController.deleteConsultant(id));
    }

    @Test
    void addConsultant_WithNullValues_ShouldHandleGracefully() {
        // This tests what happens when invalid data is passed
        // The actual behavior depends on your validation setup
        Consultant invalidConsultant = new Consultant(null, null, null);

        when(consultantService.addConsultant(any(Consultant.class)))
                .thenThrow(new IllegalArgumentException("Invalid consultant data"));

        assertThrows(IllegalArgumentException.class, () -> consultantController.addConsultant(invalidConsultant));
    }

}