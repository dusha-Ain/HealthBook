package com.rent.healthbook.profile.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfileSecurityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void profileSecurity_ShouldBeValid_WhenAllFieldsAreValid() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                "StrongPass123!",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenLastNameIsBlank() {
        ProfileSecurity profile = new ProfileSecurity(
                "",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                "StrongPass123!",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenFirstNameIsBlank() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "",
                "Konstantinovich",
                "test@example.com",
                "StrongPass123!",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenEmailIsBlank() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                "Konstantinovich",
                "",
                "StrongPass123!",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenPasswordIsBlank() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                "",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenPhoneIsBlank() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                "StrongPass123!",
                ""
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profileSecurity_ShouldBeInvalid_WhenPhonePatternIsWrong() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                "StrongPass123!",
                "invalid-phone-number"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
        boolean hasPhoneViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
        assertTrue(hasPhoneViolation);
    }

    @Test
    void profileSecurity_ShouldBeValid_WhenPatronymicIsNull() {
        ProfileSecurity profile = new ProfileSecurity(
                "Belov",
                "Andrey",
                null,
                "test@example.com",
                "StrongPass123!",
                "+79255272450"
        );

        Set<ConstraintViolation<ProfileSecurity>> violations = validator.validate(profile);

        assertTrue(violations.isEmpty());
    }
}