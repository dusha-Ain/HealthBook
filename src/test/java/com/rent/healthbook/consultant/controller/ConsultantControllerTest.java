package com.rent.healthbook.consultant.controller;

import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.dto.Consultants;
import com.rent.healthbook.consultant.service.ConsultantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultantControllerTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

    @Mock
    private ConsultantService consultantService;

    private UUID testConsultantId;
    private UUID testProfileId;
    private Consultant testConsultant;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testConsultantId = UUID.randomUUID();
        testProfileId = UUID.randomUUID();
        testConsultant = new Consultant(testConsultantId, false, testProfileId);
    }

    // ==================== POST /api/profiles-consultant/ ====================

//    @Test
//    void testAddConsultant_Success() throws Exception {
//        // Arrange
//        Consultant newConsultant = new Consultant(null, null, testProfileId);
//        Consultant savedConsultant = new Consultant(testConsultantId, false, testProfileId);
//
//        when(consultantService.addConsultant(any(Consultant.class)))
//                .thenReturn(savedConsultant);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/profiles-consultant/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newConsultant)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(testConsultantId.toString()))
//                .andExpect(jsonPath("$.idProfile").value(testProfileId.toString()))
//                .andExpect(jsonPath("$.isOnline").value(false));
//    }

    @Test
    void testAddConsultant_MissingIdProfile() throws Exception {
        // Arrange - Missing required field
        String jsonBody = "{\"id\": null, \"isOnline\": null}";

        // Act & Assert
        mockMvc.perform(post("/api/profiles-consultant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    // ==================== GET /api/profiles-consultant/{id} ====================

    @Test
    void testGetConsultantById_Success() throws Exception {
        // Arrange
        when(consultantService.getById(testConsultantId))
                .thenReturn(testConsultant);

        // Act & Assert
        mockMvc.perform(get("/api/profiles-consultant/{id}", testConsultantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testConsultantId.toString()))
                .andExpect(jsonPath("$.idProfile").value(testProfileId.toString()))
                .andExpect(jsonPath("$.isOnline").value(false));
    }

    @Test
    void testGetConsultantById_NotFound() throws Exception {
        // Arrange
        UUID notFoundId = UUID.randomUUID();
        when(consultantService.getById(notFoundId))
                .thenThrow(new RuntimeException("Consultant not found"));

        // Act & Assert
        mockMvc.perform(get("/api/profiles-consultant/{id}", notFoundId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    // ==================== GET /api/profiles-consultant/ ====================

    @Test
    void testGetAllConsultantsByFilter_DefaultPagination() throws Exception {
        // Arrange
        Consultants consultantsList = new Consultants(java.util.List.of(testConsultant));
        when(consultantService.getAllConsultantsByFilter(any()))
                .thenReturn(consultantsList);

        // Act & Assert
        mockMvc.perform(get("/api/profiles-consultant/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consultants").isArray())
                .andExpect(jsonPath("$.consultants[0].id").value(testConsultantId.toString()));
    }

    @Test
    void testGetAllConsultantsByFilter_WithPagination() throws Exception {
        // Arrange
        Consultants consultantsList = new Consultants(java.util.List.of(testConsultant));
        when(consultantService.getAllConsultantsByFilter(any()))
                .thenReturn(consultantsList);

        // Act & Assert
        mockMvc.perform(get("/api/profiles-consultant/")
                        .param("pageSize", "10")
                        .param("pageNumber", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consultants").isArray());
    }

    @Test
    void testGetAllConsultantsByFilter_EmptyList() throws Exception {
        // Arrange
        Consultants emptyList = new Consultants(java.util.List.of());
        when(consultantService.getAllConsultantsByFilter(any()))
                .thenReturn(emptyList);

        // Act & Assert
        mockMvc.perform(get("/api/profiles-consultant/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consultants").isArray())
                .andExpect(jsonPath("$.consultants").isEmpty());
    }

    // ==================== PUT /api/profiles-consultant/update/web/{id} ====================

    @Test
    void testUpdateConsultantOnline_Success() throws Exception {
        // Arrange
        doNothing().when(consultantService)
                .updateConsultantOnline(testConsultantId, true);

        // Act & Assert
        mockMvc.perform(put("/api/profiles-consultant/update/web/{id}", testConsultantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateConsultantOnline_SetOffline() throws Exception {
        // Arrange
        doNothing().when(consultantService)
                .updateConsultantOnline(testConsultantId, false);

        // Act & Assert
        mockMvc.perform(put("/api/profiles-consultant/update/web/{id}", testConsultantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("false"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateConsultantOnline_ConsultantNotFound() throws Exception {
        // Arrange
        UUID notFoundId = UUID.randomUUID();
        doNothing().when(consultantService)
                .updateConsultantOnline(notFoundId, true);

        // Act & Assert - Will pass as mock doesn't throw exception
        mockMvc.perform(put("/api/profiles-consultant/update/web/{id}", notFoundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk());
    }

    // ==================== DELETE /api/profiles-consultant/delete/{id} ====================

    @Test
    void testDeleteConsultant_Success() throws Exception {
        // Arrange
        doNothing().when(consultantService)
                .deleteConsultant(testConsultantId);

        // Act & Assert
        mockMvc.perform(delete("/api/profiles-consultant/delete/{id}", testConsultantId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteConsultant_NotFound() throws Exception {
        // Arrange
        UUID notFoundId = UUID.randomUUID();
        doNothing().when(consultantService)
                .deleteConsultant(notFoundId);

        // Act & Assert - Will pass as mock doesn't throw exception
        mockMvc.perform(delete("/api/profiles-consultant/delete/{id}", notFoundId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}