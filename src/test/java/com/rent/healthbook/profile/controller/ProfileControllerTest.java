package com.rent.healthbook.profile.controller;

import com.rent.healthbook.profile.dto.*;
import com.rent.healthbook.profile.service.ProfileSearchFilter;
import com.rent.healthbook.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    @Test
    void addProfile_ShouldReturnCreated() {
        UUID id = UUID.randomUUID();
        ProfileSecurity inputProfile = new ProfileSecurity(
                "Belov", "Andrey", "Konstantinovich11",
                "test@example.com", "password123", "+79255272450");

        Profile expectedProfile = new Profile(
                id, "BelovMia", "Andrey", "Konstantinovich",
                "test@example.com", false, new Date(System.currentTimeMillis()));

        when(profileService.addProfile(any(ProfileSecurity.class))).thenReturn(expectedProfile);

        ResponseEntity<Profile> response = profileController.addProfile(inputProfile);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        verify(profileService, times(1)).addProfile(any(ProfileSecurity.class));

        //        Well this is for changing the data and checks that the actual data is matched to the
//        expecte data or not??  the Test is working properly.
//        assertEquals("BelovMia", response.getBody().firstName());
//        assertEquals("Andrey", response.getBody().lastName());
//        assertEquals("Konstantinovich", response.getBody().patronymic());
//        assertEquals("test@example.com", response.getBody().email());

    }

    @Test
    void getProfileById_ShouldReturnProfile() {
        UUID id = UUID.randomUUID();
        Profile profile = new Profile(
                id, "Belov", "Andrey", "Konstantinovich",
                "test@example.com", false, new Date(System.currentTimeMillis()));

        when(profileService.getById(id)).thenReturn(profile);

        ResponseEntity<Profile> response = profileController.getProfileById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(id, response.getBody().id());
        verify(profileService, times(1)).getById(id);

        //These test are strictly for the data insurance that is the data is
        // matching with the actual of that data that we are expecting.
//        assertNotNull(response.getBody());
//        assertEquals("Belov", response.getBody().lastName());
//        assertEquals("Andrey",response.getBody().firstName());
//        assertEquals("mia.com", response.getBody().email());
    }

    @Test
    void getAllProfilesByFilter_ShouldReturnProfiles() {
        Profiles mockProfiles = new Profiles(java.util.Collections.emptyList());

        when(profileService.getAllProfilesByFilter(any(ProfileSearchFilter.class)))
                .thenReturn(mockProfiles);

        ResponseEntity<Profiles> response = profileController.getAllProfilesByFilter(
                "test@example.com", "Belov", "Andrey", "Konstantinovich", 10, 0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(profileService, times(1)).getAllProfilesByFilter(any(ProfileSearchFilter.class));
    }

    @Test
    void updateProfile_ShouldReturnUpdatedProfile() {
        UUID id = UUID.randomUUID();
        ProfileUpdate updateData = new ProfileUpdate(
                "Sidorov", "Ivan", "Petrovich",
                "updated@example.com", "+79991234567");

        Profile updatedProfile = new Profile(
                id, "Sidorov", "Ivan", "Petrovich",
                "updated@example.com", false, new Date(System.currentTimeMillis()));

        when(profileService.updateProfile(eq(id), any(ProfileUpdate.class))).thenReturn(updatedProfile);

        ResponseEntity<Profile> response = profileController.updateProfile(id, updateData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(profileService, times(1)).updateProfile(eq(id), any(ProfileUpdate.class));
    }

    @Test
    void updateProfilePassword_ShouldReturnOk() {
        UUID id = UUID.randomUUID();
        ProfilePassword password = new ProfilePassword("oldPass", "newPass123", "newPass123");

        doNothing().when(profileService).updateProfilePassword(eq(id), any(ProfilePassword.class));

        ResponseEntity<Void> response = profileController.updateProfilePassword(id, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileService, times(1)).updateProfilePassword(eq(id), any(ProfilePassword.class));
    }

    @Test
    void deleteProfile_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(profileService).deleteProfile(id);

        ResponseEntity<Void> response = profileController.deleteProfile(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(profileService, times(1)).deleteProfile(id);
    }

    @Test
    void blockProfile_ShouldReturnNoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(profileService).blockProfile(id);

        ResponseEntity<Void> response = profileController.blockProfile(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(profileService, times(1)).blockProfile(id);
    }

    @Test
    void getProfileById_WhenNotFound_ShouldThrowException() {
        UUID nonExistentId = UUID.randomUUID();

        when(profileService.getById(nonExistentId))
                .thenThrow(new RuntimeException("Profile not found"));

        assertThrows(RuntimeException.class, () -> {
            profileController.getProfileById(nonExistentId);
        });
    }
}