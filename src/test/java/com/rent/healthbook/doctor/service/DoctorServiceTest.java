package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.dao.DoctorDao;
import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.dto.Doctors;
import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.doctor.entity.DoctorsEntity;
import com.rent.healthbook.web.helpers.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private DoctorDao doctorDao;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void addDoctor_ShouldCreateAndReturnDoctor() {
        // Given
        UUID profileId = UUID.randomUUID();
        UUID generatedId = UUID.randomUUID();

        Date workStart = Date.valueOf("2024-01-01");
        Date workEnd = Date.valueOf("2025-12-31");

        Doctor inputDoctor = new Doctor(
                null,
                "Cardiology",
                "101",
                workStart,
                workEnd,
                Time.valueOf("00:30:00"),
                profileId
        );

        // Entity that will be saved (what mapper returns)
        DoctorEntity entityToSave = new DoctorEntity();
        entityToSave.setIdProfile(profileId);
        entityToSave.setSpecialization("Cardiology");
        entityToSave.setCabinetNumber("101");
        entityToSave.setWorkStart(workStart);
        entityToSave.setWorkEnd(workEnd);
        entityToSave.setDurationReception(Time.valueOf("00:30:00"));

        // Entity after saving
        DoctorEntity savedEntity = new DoctorEntity();
        savedEntity.setId(generatedId);
        savedEntity.setSpecialization("Cardiology");
        savedEntity.setCabinetNumber("101");
        savedEntity.setWorkStart(workStart);
        savedEntity.setWorkEnd(workEnd);
        savedEntity.setDurationReception(Time.valueOf("00:30:00"));
        savedEntity.setIdProfile(profileId);

        // Expected output DTO
        Doctor expectedOutput = new Doctor(
                generatedId,
                "Cardiology",
                "101",
                workStart,
                workEnd,
                Time.valueOf("00:30:00"),
                profileId
        );

        // Mock DAO calls
        when(doctorDao.getDoctorByProfileIdWithLock(profileId)).thenReturn(Optional.empty());
        when(doctorMapper.toEntity(inputDoctor)).thenReturn(entityToSave);
        when(doctorDao.createDoctor(entityToSave)).thenReturn(true);
        when(doctorDao.getDoctorByProfileId(profileId)).thenReturn(Optional.of(savedEntity));
        when(doctorMapper.toDomainDoctor(savedEntity)).thenReturn(expectedOutput);

        // When
        Doctor result = doctorService.addDoctor(inputDoctor);

        // Then
        assertNotNull(result);
        assertEquals(generatedId, result.id());
        assertEquals(profileId, result.idProfile());
        assertEquals("Cardiology", result.specialization());
        assertEquals("101", result.cabinetNumber());
        assertEquals(workStart, result.workStart());
        assertEquals(workEnd, result.workEnd());

        verify(doctorDao, times(1)).createDoctor(entityToSave);
    }


    @Test
    void addDoctor_ShouldThrowException_WhenDoctorAlreadyExists() {
        UUID profileId = UUID.randomUUID();
        Doctor inputDoctor = new Doctor(null, "Cardiology", "101",
                Date.valueOf("2024-01-01"), null, Time.valueOf("00:30:00"), profileId);
        DoctorEntity existingEntity = new DoctorEntity();

        when(doctorDao.getDoctorByProfileIdWithLock(profileId)).thenReturn(Optional.of(existingEntity));

        assertThrows(Exception.class, () -> doctorService.addDoctor(inputDoctor));
        verify(doctorDao, never()).createDoctor(any());
    }

    @Test
    void getDoctorById_ShouldReturnDoctor_WhenExists() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        entity.setId(id);
        entity.setSpecialization("Cardiology");
        entity.setIdProfile(profileId);
        Doctor expectedDoctor = new Doctor(id, "Cardiology", null, null, null, null, profileId);

        when(doctorDao.getDoctorById(id)).thenReturn(Optional.of(entity));
        when(doctorMapper.toDomainDoctor(entity)).thenReturn(expectedDoctor);

        Doctor result = doctorService.getDoctorById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(doctorDao, times(1)).getDoctorById(id);
    }

    @Test
    void getDoctorById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(doctorDao.getDoctorById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> doctorService.getDoctorById(id));
    }

    @Test
    void getAllDoctorsByFilter_ShouldReturnPaginatedResults() {
        Pageable filter = new Pageable();
        filter.setPageSize(10);
        filter.setPageNumber(1);

        DoctorEntity entity1 = new DoctorEntity();
        DoctorEntity entity2 = new DoctorEntity();
        DoctorsEntity doctorsEntity = new DoctorsEntity(List.of(entity1, entity2));

        Doctor doctor1 = new Doctor(UUID.randomUUID(), "Cardiology", null, null, null, null, UUID.randomUUID());
        Doctor doctor2 = new Doctor(UUID.randomUUID(), "Neurology", null, null, null, null, UUID.randomUUID());

        when(doctorDao.getAllByFilter(any(Pageable.class))).thenReturn(doctorsEntity);
        when(doctorMapper.toDomainDoctor(entity1)).thenReturn(doctor1);
        when(doctorMapper.toDomainDoctor(entity2)).thenReturn(doctor2);

        Doctors result = doctorService.getAllDoctorsByFilter(filter);

        assertNotNull(result);
        assertEquals(2, result.list.size());
        verify(doctorDao, times(1)).getAllByFilter(any(Pageable.class));
    }

    @Test
    void updateDoctor_ShouldUpdateAndReturnDoctor() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        DoctorUpdate updateData = new DoctorUpdate("Dermatology", "202",
                Date.valueOf("2024-06-01"), null, Time.valueOf("00:45:00"), profileId);

        DoctorEntity existingEntity = new DoctorEntity();
        existingEntity.setId(id);
        DoctorEntity updatedEntity = new DoctorEntity();
        updatedEntity.setId(id);
        updatedEntity.setSpecialization("Dermatology");
        Doctor expectedDoctor = new Doctor(id, "Dermatology", null, null, null, null, profileId);

        when(doctorDao.getDoctorByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(doctorMapper.toEntity(existingEntity, updateData)).thenReturn(updatedEntity);
        when(doctorDao.updateDoctor(updatedEntity)).thenReturn(true);
        when(doctorDao.getDoctorById(id)).thenReturn(Optional.of(updatedEntity));
        when(doctorMapper.toDomainDoctor(updatedEntity)).thenReturn(expectedDoctor);

        Doctor result = doctorService.updateDoctor(id, updateData);

        assertNotNull(result);
        verify(doctorDao, times(1)).updateDoctor(updatedEntity);
    }

    @Test
    void updateDoctor_ShouldThrowException_WhenDoctorNotFound() {
        UUID id = UUID.randomUUID();
        DoctorUpdate updateData = new DoctorUpdate("Dermatology", null, null, null, null, null);

        when(doctorDao.getDoctorByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> doctorService.updateDoctor(id, updateData));
        verify(doctorDao, never()).updateDoctor(any());
    }

    @Test
    void deleteDoctor_ShouldDelete_WhenDoctorExists() {
        UUID id = UUID.randomUUID();
        DoctorEntity existingEntity = new DoctorEntity();

        when(doctorDao.getDoctorByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(doctorDao.deleteDoctor(id)).thenReturn(true);

        doctorService.deleteDoctor(id);

        verify(doctorDao, times(1)).deleteDoctor(id);
    }

    @Test
    void deleteDoctor_ShouldThrowException_WhenDoctorNotFound() {
        UUID id = UUID.randomUUID();
        when(doctorDao.getDoctorByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> doctorService.deleteDoctor(id));
        verify(doctorDao, never()).deleteDoctor(any());
    }
}