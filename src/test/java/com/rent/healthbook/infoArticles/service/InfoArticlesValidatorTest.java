package com.rent.healthbook.infoArticles.service;

import com.rent.healthbook.infoArticles.entity.InfoArticleEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InfoArticlesValidatorTest {

    @Test
    void validateArticleExistsAfterUpdate_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        InfoArticleEntity entity = new InfoArticleEntity();
        Optional<InfoArticleEntity> optional = Optional.of(entity);

        InfoArticleEntity result = InfoArticlesValidator.validateArticleExistsAfterUpdate(optional, id);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void validateArticleExistsAfterUpdate_ShouldThrowException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<InfoArticleEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> InfoArticlesValidator.validateArticleExistsAfterUpdate(optional, id));
    }

    @Test
    void validateArticleCheckExists_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        InfoArticleEntity entity = new InfoArticleEntity();
        Optional<InfoArticleEntity> optional = Optional.of(entity);

        InfoArticleEntity result = InfoArticlesValidator.validateArticleCheckExists(optional, id);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void validateArticleCheckExists_ShouldThrowValidationException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<InfoArticleEntity> optional = Optional.empty();

        assertThrows(ValidationException.class,
                () -> InfoArticlesValidator.validateArticleCheckExists(optional, id));
    }

    @Test
    void validateCreationResult_ShouldReturnId_WhenIdIsNotNull() {
        UUID id = UUID.randomUUID();

        UUID result = InfoArticlesValidator.validateCreationResult(id);

        assertEquals(id, result);
    }

    @Test
    void validateCreationResult_ShouldThrowValidationException_WhenIdIsNull() {
        assertThrows(ValidationException.class,
                () -> InfoArticlesValidator.validateCreationResult(null));
    }

    @Test
    void validateUpdateResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> InfoArticlesValidator.validateUpdateResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> InfoArticlesValidator.validateUpdateResult(false, UUID.randomUUID()));
    }

    @Test
    void validateUpdateStatusResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> InfoArticlesValidator.validateUpdateStatusResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdateStatusResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> InfoArticlesValidator.validateUpdateStatusResult(false, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> InfoArticlesValidator.validateDeleteResult(true, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> InfoArticlesValidator.validateDeleteResult(false, UUID.randomUUID()));
    }

    @Test
    void validateIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        UUID id = UUID.randomUUID();
        InfoArticleEntity entity = new InfoArticleEntity();
        List<InfoArticleEntity> list = List.of(entity);

        Optional<InfoArticleEntity> result = InfoArticlesValidator.validateIdSearchResult(list, id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validateIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        UUID id = UUID.randomUUID();
        List<InfoArticleEntity> list = List.of();

        Optional<InfoArticleEntity> result = InfoArticlesValidator.validateIdSearchResult(list, id);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        UUID id = UUID.randomUUID();
        List<InfoArticleEntity> list = List.of(new InfoArticleEntity(), new InfoArticleEntity());

        assertThrows(EntityToManyResultException.class,
                () -> InfoArticlesValidator.validateIdSearchResult(list, id));
    }
}