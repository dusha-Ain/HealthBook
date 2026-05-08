package com.rent.healthbook.web.kafka.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProfileEventTest {

    @Test
    void testNoArgsConstructor() {
        ProfileEvent event = new ProfileEvent();
        assertNull(event.getEventType());
        assertNull(event.getProfileId());
        assertNull(event.getName());
        assertNull(event.getRole());
        assertNull(event.getTimestamp());
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        String eventType = "PROFILE_CREATED";
        UUID profileId = UUID.randomUUID();
        String name = "Dr. House";
        String role = "DOCTOR";
        LocalDateTime timestamp = LocalDateTime.now();

        // When
        ProfileEvent event = new ProfileEvent(eventType, profileId, name, role, timestamp);

        // Then
        assertEquals(eventType, event.getEventType());
        assertEquals(profileId, event.getProfileId());
        assertEquals(name, event.getName());
        assertEquals(role, event.getRole());
        assertEquals(timestamp, event.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        ProfileEvent event = new ProfileEvent();

        String eventType = "PROFILE_UPDATED";
        UUID profileId = UUID.randomUUID();
        String name = "John Doe";
        String role = "CONSULTANT";
        LocalDateTime timestamp = LocalDateTime.now();

        event.setEventType(eventType);
        event.setProfileId(profileId);
        event.setName(name);
        event.setRole(role);
        event.setTimestamp(timestamp);

        assertEquals(eventType, event.getEventType());
        assertEquals(profileId, event.getProfileId());
        assertEquals(name, event.getName());
        assertEquals(role, event.getRole());
        assertEquals(timestamp, event.getTimestamp());
    }

    @Test
    void testToStringContainsFields() {
        UUID profileId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        LocalDateTime timestamp = LocalDateTime.of(2025, 5, 9, 14, 30, 0);
        ProfileEvent event = new ProfileEvent(
                "PROFILE_DELETED",
                profileId,
                "Alice",
                "ADMIN",
                timestamp
        );

        String str = event.toString();
        assertTrue(str.contains("PROFILE_DELETED"));
        assertTrue(str.contains(profileId.toString()));
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("ADMIN"));
        assertTrue(str.contains(timestamp.toString()));
    }
}