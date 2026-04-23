package com.rent.healthbook.profile.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProfilePasswordTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void profilePassword_ShouldBeValid_WhenAllFieldsAreValid() {
        ProfilePassword password = new ProfilePassword(
                "oldPassword123",
                "newStrongPass123!",
                "newStrongPass123!"
        );

        Set<ConstraintViolation<ProfilePassword>> violations = validator.validate(password);

        assertTrue(violations.isEmpty());
    }

    @Test
    void profilePassword_ShouldBeInvalid_WhenCurrentPasswordIsBlank() {
        ProfilePassword password = new ProfilePassword(
                "",
                "newStrongPass123!",
                "newStrongPass123!"
        );

        Set<ConstraintViolation<ProfilePassword>> violations = validator.validate(password);

        assertFalse(violations.isEmpty());
        boolean hasCurrentPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("currentPassword"));
        assertTrue(hasCurrentPasswordViolation);
    }

    @Test
    void profilePassword_ShouldBeInvalid_WhenNewPasswordIsBlank() {
        ProfilePassword password = new ProfilePassword(
                "oldPassword123",
                "",
                "newStrongPass123!"
        );

        Set<ConstraintViolation<ProfilePassword>> violations = validator.validate(password);

        assertFalse(violations.isEmpty());
        boolean hasNewPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("newPassword"));
        assertTrue(hasNewPasswordViolation);
    }

    @Test
    void profilePassword_ShouldBeInvalid_WhenNewPasswordIsTooShort() {
        ProfilePassword password = new ProfilePassword(
                "oldPassword123",
                "short",
                "short"
        );

        Set<ConstraintViolation<ProfilePassword>> violations = validator.validate(password);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profilePassword_ShouldBeInvalid_WhenConfirmPasswordIsBlank() {
        ProfilePassword password = new ProfilePassword(
                "oldPassword123",
                "newStrongPass123!",
                ""
        );

        Set<ConstraintViolation<ProfilePassword>> violations = validator.validate(password);

        assertFalse(violations.isEmpty());
        boolean hasConfirmPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("confirmPassword"));
        assertTrue(hasConfirmPasswordViolation);
    }
}