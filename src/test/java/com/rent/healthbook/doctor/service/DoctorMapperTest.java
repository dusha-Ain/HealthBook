package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorMapperTest {

    private final DoctorMapper mapper = new DoctorMapper();

    @Test
    void toEntity_ShouldConvertDoctorDtoToEntity() {
        UUID profileId = UUID.randomUUID();
        Doctor doctor = new Doctor(null, "Cardiology", "101",
                Date.valueOf("2024-01-01"), null, Time.valueOf("00:30:00"), profileId);

        DoctorEntity entity = mapper.toEntity(doctor);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Cardiology", entity.getSpecialization());
        assertEquals("101", entity.getCabinetNumber());
        assertEquals(profileId, entity.getIdProfile());
    }

    @Test
    void toEntity_ShouldThrowException_WhenDoctorIsNull() {
        assertThrows(ValidationException.class, () -> mapper.toEntity(null));
    }

    @Test
    void toDomainDoctor_ShouldConvertEntityToDoctorDto() {
        UUID id = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        entity.setId(id);
        entity.setSpecialization("Cardiology");
        entity.setCabinetNumber("101");
        entity.setWorkStart(Date.valueOf("2024-01-01"));
        entity.setDurationReception(Time.valueOf("00:30:00"));
        entity.setIdProfile(profileId);

        Doctor doctor = mapper.toDomainDoctor(entity);

        assertNotNull(doctor);
        assertEquals(id, doctor.id());
        assertEquals("Cardiology", doctor.specialization());
        assertEquals("101", doctor.cabinetNumber());
        assertEquals(profileId, doctor.idProfile());
    }

    @Test
    void toDomainDoctor_ShouldThrowException_WhenEntityIsNull() {
        assertThrows(ValidationException.class, () -> mapper.toDomainDoctor(null));
    }

    @Test
    void toEntity_WithUpdate_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        DoctorEntity existingEntity = new DoctorEntity();
        existingEntity.setId(id);
        existingEntity.setSpecialization("OldSpecialization");

        DoctorUpdate update = new DoctorUpdate("NewSpecialization", null, null, null, null, null);

        DoctorEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("NewSpecialization", result.getSpecialization());
        assertEquals(id, result.getId());
    }

    @Test
    void toEntity_WithUpdate_ShouldReturnOriginal_WhenUpdateIsNull() {
        DoctorEntity existingEntity = new DoctorEntity();

        DoctorEntity result = mapper.toEntity(existingEntity, null);

        assertEquals(existingEntity, result);
    }

    @Test
    void toEntity_WithUpdate_ShouldOnlyUpdateNonNullFields() {
        DoctorEntity existingEntity = new DoctorEntity();
        existingEntity.setSpecialization("OldSpec");
        existingEntity.setCabinetNumber("OldCabinet");

        DoctorUpdate update = new DoctorUpdate("NewSpec", null, null, null, null, null);

        DoctorEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("NewSpec", result.getSpecialization());
        assertEquals("OldCabinet", result.getCabinetNumber());
    }
}