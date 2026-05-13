package com.rent.healthbook.web.kafka.dto;

import com.rent.healthbook.profile.dto.ProfileKafka;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationResponseTest {

    @Test
    void testNoArgsConstructor() {
        UserValidationResponse response = new UserValidationResponse();
        assertNull(response.getIdRequest());
        assertFalse(response.isExists()); // default boolean false
        assertNull(response.getProfile());
        assertNull(response.getErrorMessage());
    }

    @Test
    void testSettersAndGetters() {
        UserValidationResponse response = new UserValidationResponse();

        String idRequest = "req-456";
        boolean exists = true;
        ProfileKafka profile = new ProfileKafka("789", "Dr. Smith", "DOCTOR");
        String errorMessage = "User not found";

        response.setIdRequest(idRequest);
        response.setExists(exists);
        response.setProfile(profile);
        response.setErrorMessage(errorMessage);

        assertEquals(idRequest, response.getIdRequest());
        assertTrue(response.isExists());
        assertSame(profile, response.getProfile());
        assertEquals(errorMessage, response.getErrorMessage());
    }

    @Test
    void testProfileCanBeNull() {
        UserValidationResponse response = new UserValidationResponse();
        response.setProfile(null);
        assertNull(response.getProfile());
    }

    @Test
    void testExistsFlagDefaultsToFalse() {
        UserValidationResponse response = new UserValidationResponse();
        assertFalse(response.isExists());
        response.setExists(true);
        assertTrue(response.isExists());
        response.setExists(false);
        assertFalse(response.isExists());
    }
}