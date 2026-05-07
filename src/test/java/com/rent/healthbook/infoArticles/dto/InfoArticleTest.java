package com.rent.healthbook.infoArticles.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InfoArticleTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void infoArticle_ShouldBeValid_WhenAllFieldsAreValid() {
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Step by step guide for registration",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void infoArticle_ShouldBeInvalid_WhenTitleIsBlank() {
        InfoArticle article = new InfoArticle(
                null,
                "",
                "Content here",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertFalse(violations.isEmpty());
        boolean hasTitleViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title"));
        assertTrue(hasTitleViolation, "Should have validation error for blank title");
    }

    @Test
    void infoArticle_ShouldBeInvalid_WhenTitleExceedsMaxLength() {
        String longTitle = "This title is way too long and exceeds the maximum allowed length of 50 characters";
        InfoArticle article = new InfoArticle(
                null,
                longTitle,
                "Content here",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertFalse(violations.isEmpty());
    }

    @Test
    void infoArticle_ShouldBeValid_WhenContentIsBlank() {
        // Content can be blank according to validation (no @NotBlank annotation)
        InfoArticle article = new InfoArticle(
                null,
                "Valid Title",
                "",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertTrue(violations.isEmpty(), "Content can be blank");
    }

    @Test
    void infoArticle_ShouldBeInvalid_WhenIdProfileIsNull() {
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Content here",
                null,
                null,
                null
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertFalse(violations.isEmpty());
        boolean hasIdProfileViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("idProfile"));
        assertTrue(hasIdProfileViolation, "Should have validation error for null idProfile");
    }

    @Test
    void infoArticle_ShouldBeValid_WhenCreateRequest() {
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Content here",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);
        assertTrue(violations.isEmpty());
    }

    @Test
    void infoArticle_ShouldBeValid_WhenCreatedIsNull() {
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Content here",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertTrue(violations.isEmpty(), "Created can be null (will be set by system)");
    }

    @Test
    void infoArticle_ShouldBeValid_WhenIsPublishedIsNull() {
        InfoArticle article = new InfoArticle(
                null,
                "How to Register",
                "Content here",
                null,
                null,
                UUID.randomUUID()
        );

        Set<ConstraintViolation<InfoArticle>> violations = validator.validate(article);

        assertTrue(violations.isEmpty(), "isPublished can be null (will be set to false by system)");
    }
}