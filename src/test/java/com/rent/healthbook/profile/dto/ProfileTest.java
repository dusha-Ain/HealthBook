package com.rent.healthbook.profile.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void profile_ShouldBeValid_WhenAllFieldsAreValid() {
        Profile profile = new Profile(
                null,
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void profile_ShouldBeInvalid_WhenLastNameIsBlank() {
        Profile profile = new Profile(
                null,
                "",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
        boolean hasLastNameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
        assertTrue(hasLastNameViolation);
    }

    @Test
    void profile_ShouldBeInvalid_WhenLastNameExceedsMaxLength() {
        Profile profile = new Profile(
                null,
                "ThisLastNameIsWayTooLongAndExceeds25Characters",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profile_ShouldBeInvalid_WhenFirstNameIsBlank() {
        Profile profile = new Profile(
                null,
                "Belov",
                "",
                "Konstantinovich",
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
        boolean hasFirstNameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
        assertTrue(hasFirstNameViolation);
    }

    @Test
    void profile_ShouldBeInvalid_WhenEmailIsBlank() {
        Profile profile = new Profile(
                null,
                "Belov",
                "Andrey",
                "Konstantinovich",
                "",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailViolation);
    }

    @Test
    void profile_ShouldBeInvalid_WhenEmailExceedsMaxLength() {
        Profile profile = new Profile(
                null,
                "Belov",
                "Andrey",
                "Konstantinovich",
                "thisemailiswaytoolongandexceeds20chars@test.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertFalse(violations.isEmpty());
    }

    @Test
    void profile_ShouldBeValid_WhenPatronymicIsNull() {
        Profile profile = new Profile(
                null,
                "Belov",
                "Andrey",
                null,
                "test@example.com",
                null,
                null
        );

        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);

        assertTrue(violations.isEmpty());
    }

    @Test
    void profile_ShouldBeValid_WhenIdIsNotNull() {
        // Id can have value when retrieving from database
        Profile profile = new Profile(
                //UUID.randomUUID(),
                null,
                "Belov",
                "Andrey",
                "Konstantinovich",
                "test@example.com",
                null,
                null
        );
        Set<ConstraintViolation<Profile>> violations = validator.validate(profile);
        assertTrue(violations.isEmpty(), "Id can be  null for existing profiles");

        // Here was the idea of the profile which the id could be null or not
//        violations.forEach(v ->
//                System.out.println(v.getPropertyPath() + " -> " + v.getMessage())
//        );
    }
}