package com.rent.healthbook.web.kafka.dto;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserValidationRequestTest {

    @Test
    void testNoArgsConstructor() {
        UserValidationRequest request = new UserValidationRequest();
        assertNull(request.getIdRequest());
        assertNull(request.getIdUser());
        assertNull(request.getAction());
    }

    @Test
    void testSettersAndGetters() {
        UserValidationRequest request = new UserValidationRequest();

        String idRequest = "req-123";
        UUID idUser = UUID.randomUUID();
        String action = "VALIDATE";

        request.setIdRequest(idRequest);
        request.setIdUser(idUser);
        request.setAction(action);

        assertEquals(idRequest, request.getIdRequest());
        assertEquals(idUser, request.getIdUser());
        assertEquals(action, request.getAction());
    }

    @Test
    void testCanSetAndGetMultipleValues() {
        UserValidationRequest request = new UserValidationRequest();

        UUID firstUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        request.setIdUser(firstUuid);
        assertEquals(firstUuid, request.getIdUser());

        UUID secondUuid = UUID.randomUUID();
        request.setIdUser(secondUuid);
        assertEquals(secondUuid, request.getIdUser());

        request.setIdRequest("another-id");
        assertEquals("another-id", request.getIdRequest());

        request.setAction("DELETE");
        assertEquals("DELETE", request.getAction());
    }
}