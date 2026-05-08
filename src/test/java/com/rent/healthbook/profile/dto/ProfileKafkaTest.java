package com.rent.healthbook.profile.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProfileKafkaTest {

    @Test
    void testNoArgsConstructor() {
        ProfileKafka profile = new ProfileKafka();
        assertNull(profile.getId());
        assertNull(profile.getName());
        assertNull(profile.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        String id = "123";
        String name = "Dr. Smith";
        String role = "DOCTOR";

        // When
        ProfileKafka profile = new ProfileKafka(id, name, role);

        // Then
        assertEquals(id, profile.getId());
        assertEquals(name, profile.getName());
        assertEquals(role, profile.getRole());
    }

    @Test
    void testSettersAndGetters() {
        ProfileKafka profile = new ProfileKafka();

        String id = "456";
        String name = "Jane Doe";
        String role = "CONSULTANT";

        profile.setId(id);
        profile.setName(name);
        profile.setRole(role);

        assertEquals(id, profile.getId());
        assertEquals(name, profile.getName());
        assertEquals(role, profile.getRole());
    }
}