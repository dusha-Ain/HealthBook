package com.rent.healthbook.doctor.controller;

import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.dto.Doctors;
import com.rent.healthbook.doctor.service.DoctorService;
import com.rent.healthbook.web.helpers.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Test
    void addDoctor_ShouldReturnCreated() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        Doctor doctor = new Doctor(null, "Cardiology", "101",
                new Date(System.currentTimeMillis()), null,
                Time.valueOf("00:30:00"), profileId);
        Doctor savedDoctor = new Doctor(id, "Cardiology", "101",
                new Date(System.currentTimeMillis()), null,
                Time.valueOf("00:30:00"), profileId);

        when(doctorService.addDoctor(any(Doctor.class))).thenReturn(savedDoctor);

        ResponseEntity<Doctor> response = doctorController.addDoctor(doctor);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        verify(doctorService, times(1)).addDoctor(any(Doctor.class));
    }

    @Test
    void getDoctorById_ShouldReturnDoctor() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        Doctor doctor = new Doctor(id, "Neurology", "202",
                new Date(System.currentTimeMillis()), null,
                Time.valueOf("00:45:00"), profileId);

        when(doctorService.getDoctorById(id)).thenReturn(doctor);

        ResponseEntity<Doctor> response = doctorController.getDoctorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert response.getBody() != null;
        assertEquals(id, response.getBody().id());
        verify(doctorService, times(1)).getDoctorById(id);
    }

    @Test
    void getAllDoctorsByFilter_ShouldReturnDoctors() {
        Doctors mockDoctors = new Doctors(java.util.Collections.emptyList());

        when(doctorService.getAllDoctorsByFilter(any(Pageable.class)))
                .thenReturn(mockDoctors);

        ResponseEntity<Doctors> response = doctorController.getAllDoctorsByFilter(10, 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(doctorService, times(1)).getAllDoctorsByFilter(any(Pageable.class));
    }

    @Test
    void updateDoctor_ShouldReturnUpdatedDoctor() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        DoctorUpdate updateData = new DoctorUpdate("Dermatology", "303",
                new Date(System.currentTimeMillis()), null,
                Time.valueOf("00:30:00"), profileId);
        Doctor updatedDoctor = new Doctor(id, "Dermatology", "303",
                new Date(System.currentTimeMillis()), null,
                Time.valueOf("00:30:00"), profileId);

        when(doctorService.updateDoctor(eq(id), any(DoctorUpdate.class))).thenReturn(updatedDoctor);

        ResponseEntity<Doctor> response = doctorController.updateDoctor(id, updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(doctorService, times(1)).updateDoctor(eq(id), any(DoctorUpdate.class));
    }

    @Test
    void deleteDoctor_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(doctorService).deleteDoctor(id);

        ResponseEntity<Void> response = doctorController.deleteDoctor(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService, times(1)).deleteDoctor(id);
    }

    @Test
    void getDoctorById_WhenNotFound_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        when(doctorService.getDoctorById(nonExistentId))
                .thenThrow(new RuntimeException("Doctor not found"));

        assertThrows(RuntimeException.class, () -> doctorController.getDoctorById(nonExistentId));
    }

    @Test
    void deleteDoctor_WhenServiceThrowsException_ShouldPropagate() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Database error"))
                .when(doctorService).deleteDoctor(id);

        assertThrows(RuntimeException.class, () -> doctorController.deleteDoctor(id));
    }
}