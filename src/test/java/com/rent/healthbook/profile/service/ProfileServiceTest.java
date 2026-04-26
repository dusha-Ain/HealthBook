package com.rent.healthbook.profile.service;

import com.rent.healthbook.profile.dao.ProfileDao;
import com.rent.healthbook.profile.dto.*;
import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.profile.entity.ProfilesEntity;
import com.rent.healthbook.web.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileDao profileDao;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void addProfile_ShouldCreateAndReturnProfile() {
        String phone = "+79255272450";
        UUID generatedId = UUID.randomUUID();

        ProfileSecurity inputProfile = new ProfileSecurity(
                "Belov", "Andrey", "Konstantinovich",
                "test@example.com", "password123", phone);

        ProfileEntity entityToSave = new ProfileEntity();
        ProfileEntity savedEntity = new ProfileEntity();
        savedEntity.setId(generatedId);
        savedEntity.setLastName("Belov");
        savedEntity.setFirstName("Andrey");
        savedEntity.setEmail("test@example.com");
        savedEntity.setPhone(phone);

        Profile expectedOutput = new Profile(
                generatedId, "Belov", "Andrey", "Konstantinovich",
                "test@example.com", false, new Date(System.currentTimeMillis()));

        when(profileDao.getProfileByPhoneWithLock(phone)).thenReturn(Optional.empty());
        when(profileMapper.toEntity(inputProfile)).thenReturn(entityToSave);
        when(profileDao.createProfile(entityToSave)).thenReturn(true);
        when(profileDao.getProfileByPhone(phone)).thenReturn(Optional.of(savedEntity));
        when(profileMapper.toDomainProfile(savedEntity)).thenReturn(expectedOutput);

        Profile result = profileService.addProfile(inputProfile);

        assertNotNull(result);
        assertEquals(generatedId, result.id());
        verify(profileDao, times(1)).createProfile(entityToSave);
    }

    @Test
    void addProfile_ShouldThrowException_WhenProfileAlreadyExists() {
        String phone = "+79255272450";
        ProfileSecurity inputProfile = new ProfileSecurity(
                "Belov", "Andrey", "Konstantinovich",
                "test@example.com", "password123", phone);
        ProfileEntity existingEntity = new ProfileEntity();

        when(profileDao.getProfileByPhoneWithLock(phone)).thenReturn(Optional.of(existingEntity));

        assertThrows(Exception.class, () -> profileService.addProfile(inputProfile));
        verify(profileDao, never()).createProfile(any());
    }

    @Test
    void getById_ShouldReturnProfile_WhenExists() {
        UUID id = UUID.randomUUID();
        ProfileEntity entity = new ProfileEntity();
        entity.setId(id);
        Profile expectedProfile = new Profile(
                id, "Belov", "Andrey", null, "test@example.com", false, null);

        when(profileDao.getById(id)).thenReturn(Optional.of(entity));
        when(profileMapper.toDomainProfile(entity)).thenReturn(expectedProfile);

        Profile result = profileService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(profileDao, times(1)).getById(id);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(profileDao.getById(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> profileService.getById(id));
    }

    @Test
    void getAllProfilesByFilter_ShouldReturnPaginatedResults() {
        ProfileSearchFilter filter = new ProfileSearchFilter(
                "test@example.com", "Belov", "Andrey", "Konstantinovich", 10, 1);

        ProfileEntity entity1 = new ProfileEntity();
        ProfileEntity entity2 = new ProfileEntity();
        ProfilesEntity profilesEntity = new ProfilesEntity(List.of(entity1, entity2));

        Profile profile1 = new Profile(UUID.randomUUID(), "Belov", "Andrey", null, "test1@example.com", false, null);
        Profile profile2 = new Profile(UUID.randomUUID(), "Sidorov", "Ivan", null, "test2@example.com", false, null);

        when(profileDao.getAllByFilter(any(ProfileSearchFilter.class), any(Pageable.class)))
                .thenReturn(profilesEntity);
        when(profileMapper.toDomainProfile(entity1)).thenReturn(profile1);
        when(profileMapper.toDomainProfile(entity2)).thenReturn(profile2);

        Profiles result = profileService.getAllProfilesByFilter(filter);

        assertNotNull(result);
        assertEquals(2, result.list.size());
        verify(profileDao, times(1)).getAllByFilter(any(ProfileSearchFilter.class), any(Pageable.class));
    }

    @Test
    void updateProfile_ShouldUpdateAndReturnProfile() {
        UUID id = UUID.randomUUID();
        ProfileUpdate updateData = new ProfileUpdate(
                "Sidorov", "Ivan", "Petrovich",
                "updated@example.com", "+79991234567");

        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setId(id);
        ProfileEntity updatedEntity = new ProfileEntity();
        updatedEntity.setId(id);
        Profile expectedProfile = new Profile(
                id, "Sidorov", "Ivan", "Petrovich",
                "updated@example.com", false, null);

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(profileMapper.toEntity(existingEntity, updateData)).thenReturn(updatedEntity);
        when(profileDao.update(updatedEntity)).thenReturn(true);
        when(profileDao.getById(id)).thenReturn(Optional.of(updatedEntity));
        when(profileMapper.toDomainProfile(updatedEntity)).thenReturn(expectedProfile);

        Profile result = profileService.updateProfile(id, updateData);

        assertNotNull(result);
        verify(profileDao, times(1)).update(updatedEntity);
    }

    @Test
    void deleteProfile_ShouldDelete_WhenExists() {
        UUID id = UUID.randomUUID();
        ProfileEntity existingEntity = new ProfileEntity();

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(profileDao.delete(id)).thenReturn(true);

        profileService.deleteProfile(id);

        verify(profileDao, times(1)).delete(id);
    }

    @Test
    void deleteProfile_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> profileService.deleteProfile(id));
        verify(profileDao, never()).delete(any());
    }

    @Test
    void updateProfilePassword_ShouldUpdatePassword_WhenValid() {
        UUID id = UUID.randomUUID();
        ProfilePassword password = new ProfilePassword("oldPass123", "newPass123", "newPass123");
        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setPassword("oldPass123");

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(profileDao.updatePassword(id, "newPass123")).thenReturn(true);

        profileService.updateProfilePassword(id, password);

        verify(profileDao, times(1)).updatePassword(id, "newPass123");
    }

    @Test
    void updateProfilePassword_ShouldThrowException_WhenOldPasswordWrong() {
        UUID id = UUID.randomUUID();
        ProfilePassword password = new ProfilePassword("wrongPass", "newPass123", "newPass123");
        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setPassword("correctPass");

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));

        assertThrows(Exception.class, () -> profileService.updateProfilePassword(id, password));
        verify(profileDao, never()).updatePassword(any(), any());
    }

    @Test
    void updateProfilePassword_ShouldThrowException_WhenPasswordsDontMatch() {
        UUID id = UUID.randomUUID();
        ProfilePassword password = new ProfilePassword("oldPass", "newPass123", "differentPass");
        ProfileEntity existingEntity = new ProfileEntity();
        existingEntity.setPassword("oldPass");

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));

        assertThrows(Exception.class, () -> profileService.updateProfilePassword(id, password));
        verify(profileDao, never()).updatePassword(any(), any());
    }

    @Test
    void blockProfile_ShouldBlockProfile_WhenExists() {
        UUID id = UUID.randomUUID();
        ProfileEntity existingEntity = new ProfileEntity();

        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.of(existingEntity));
        when(profileDao.block(id)).thenReturn(true);

        profileService.blockProfile(id);

        verify(profileDao, times(1)).block(id);
    }

    @Test
    void blockProfile_ShouldThrowException_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(profileDao.getByIdWithLock(id)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> profileService.blockProfile(id));
        verify(profileDao, never()).block(any());
    }
}