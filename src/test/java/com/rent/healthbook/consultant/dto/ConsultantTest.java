package com.rent.healthbook.consultant.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConsultantTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void consultant_ShouldBeValid_WhenIdAndIsOnlineAreNull_IdProfileIsNotNull() {
        Consultant consultant = new Consultant(null, null, UUID.randomUUID());

        Set<ConstraintViolation<Consultant>> violations = validator.validate(consultant);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void consultant_ShouldBeInvalid_WhenIdProfileIsNull() {
        Consultant consultant = new Consultant(null, null, null);

        Set<ConstraintViolation<Consultant>> violations = validator.validate(consultant);

        assertFalse(violations.isEmpty());
        boolean hasIdProfileViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("idProfile"));
        assertTrue(hasIdProfileViolation, "Should have validation error for null idProfile");
    }

    @Test
    void consultant_ShouldBeInvalid_WhenIdIsNotNull() {
        // ID should be null for create operations
        Consultant consultant = new Consultant(UUID.randomUUID(), null, UUID.randomUUID());

        Set<ConstraintViolation<Consultant>> violations = validator.validate(consultant);

        assertFalse(violations.isEmpty());
        boolean hasIdViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id"));
        assertTrue(hasIdViolation, "Should have validation error when id is not null");
    }

    @Test
    void consultant_ShouldBeInvalid_WhenIsOnlineIsNotNull() {
        // isOnline should be null for create operations
        Consultant consultant = new Consultant(null, true, UUID.randomUUID());

        Set<ConstraintViolation<Consultant>> violations = validator.validate(consultant);

        assertFalse(violations.isEmpty());
        boolean hasIsOnlineViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("isOnline"));
        assertTrue(hasIsOnlineViolation, "Should have validation error when isOnline is not null");
    }
}