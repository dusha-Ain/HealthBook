package com.rent.healthbook.profile.service;

import com.rent.healthbook.profile.dto.Profile;
import com.rent.healthbook.profile.dto.ProfileSecurity;
import com.rent.healthbook.profile.dto.ProfileUpdate;
import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProfileMapperTest {

    private ProfileMapper mapper = new ProfileMapper();

    @Test
    void toEntity_FromProfileSecurity_ShouldConvert() {
        ProfileSecurity security = new ProfileSecurity(
                "Belov", "Andrey", "Konstantinovich",
                "test@example.com", "password123", "+79255272450");

        ProfileEntity entity = mapper.toEntity(security);

        assertNotNull(entity);
        assertEquals("Belov", entity.getLastName());
        assertEquals("Andrey", entity.getFirstName());
        assertEquals("Konstantinovich", entity.getPatronymic());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("password123", entity.getPassword());
        assertEquals("+79255272450", entity.getPhone());
    }

    @Test
    void toEntity_FromProfileSecurity_ShouldThrowException_WhenNull() {
        assertThrows(ValidationException.class, () -> mapper.toEntity((ProfileSecurity) null));
    }

    @Test
    void toEntity_FromProfileUpdate_ShouldUpdateExistingEntity() {
        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setLastName("OldName");
        existingEntity.setFirstName("Old");

        ProfileUpdate update = new ProfileUpdate(
                "NewName", "New", "NewPatronymic",
                "new@example.com", "+79991234567");

        ProfileEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("NewName", result.getLastName());
        assertEquals("New", result.getFirstName());
        assertEquals("NewPatronymic", result.getPatronymic());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("+79991234567", result.getPhone());
    }

    @Test
    void toEntity_FromProfileUpdate_ShouldReturnOriginal_WhenUpdateIsNull() {
        ProfileEntity existingEntity = new ProfileEntity();

        ProfileEntity result = mapper.toEntity(existingEntity, null);

        assertEquals(existingEntity, result);
    }

    @Test
    void toEntity_FromProfileUpdate_ShouldOnlyUpdateNonNullFields() {
        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setLastName("OldName");
        existingEntity.setFirstName("OldFirstName");

        ProfileUpdate update = new ProfileUpdate(
                "NewName", null, null, null, null);

        ProfileEntity result = mapper.toEntity(existingEntity, update);

        assertEquals("NewName", result.getLastName());
        assertEquals("OldFirstName", result.getFirstName());
    }

    @Test
    void toDomainProfile_ShouldConvertEntityToProfile() {
        UUID id = UUID.randomUUID();
        ProfileEntity entity = new ProfileEntity();
        entity.setId(id);
        entity.setLastName("Belov");
        entity.setFirstName("Andrey");
        entity.setPatronymic("Konstantinovich");
        entity.setEmail("test@example.com");
        entity.setFired(false);
        entity.setRegistrationDate(new Date(System.currentTimeMillis()));

        Profile profile = mapper.toDomainProfile(entity);

        assertNotNull(profile);
        assertEquals(id, profile.id());
        assertEquals("Belov", profile.lastName());
        assertEquals("Andrey", profile.firstName());
        assertEquals("Konstantinovich", profile.patronymic());
        assertEquals("test@example.com", profile.email());
        assertFalse(profile.is_fired());
    }

    @Test
    void toDomainProfile_ShouldThrowException_WhenEntityIsNull() {
        assertThrows(Exception.class, () -> mapper.toDomainProfile(null));
    }
}