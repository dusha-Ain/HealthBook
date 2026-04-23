package com.rent.healthbook.doctor.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void doctor_ShouldBeValid_WhenAllFieldsAreValid() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void doctor_ShouldBeInvalid_WhenSpecializationIsBlank() {
        Doctor doctor = new Doctor(
                null,
                "",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
        boolean hasSpecializationViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("specialization"));
        assertTrue(hasSpecializationViolation, "Should have validation error for blank specialization");
    }

    @Test
    void doctor_ShouldBeInvalid_WhenSpecializationExceedsMaxLength() {
        Doctor doctor = new Doctor(
                null,
                "ThisIsAVeryLongSpecializationNameExceeding20Chars",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
    }

    @Test
    void doctor_ShouldBeInvalid_WhenCabinetNumberExceedsMaxLength() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                "ThisCabinetNumberIsWayTooLong123456789",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
    }

    @Test
    void doctor_ShouldBeInvalid_WhenWorkStartIsNull() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                "101",
                null,
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
        boolean hasWorkStartViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("workStart"));
        assertTrue(hasWorkStartViolation, "Should have validation error for null workStart");
    }

    @Test
    void doctor_ShouldBeInvalid_WhenIdProfileIsNull() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                null
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
        boolean hasIdProfileViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("idProfile"));
        assertTrue(hasIdProfileViolation, "Should have validation error for null idProfile");
    }

    @Test
    void doctor_ShouldBeValid_WhenCabinetNumberIsNull() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                null,
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertTrue(violations.isEmpty(), "Cabinet number can be null");
    }

    @Test
    void doctor_ShouldBeValid_WhenWorkEndIsNull() {
        Doctor doctor = new Doctor(
                null,
                "Cardiology",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertTrue(violations.isEmpty(), "Work end can be null");
    }

    @Test
    void doctor_ShouldBeInvalid_WhenIdIsNotNull() {
        Doctor doctor = new Doctor(
                UUID.randomUUID(),
                "Cardiology",
                "101",
                new Date(System.currentTimeMillis()),
                null,
                Time.valueOf("00:30:00"),
                UUID.randomUUID()
        );

        Set<ConstraintViolation<Doctor>> violations = validator.validate(doctor);

        assertFalse(violations.isEmpty());
        boolean hasIdViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("id"));
        assertTrue(hasIdViolation, "Should have validation error when id is not null");
    }
}