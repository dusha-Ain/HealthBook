package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConsultantValidatorTest {

    @Test
    void validateConsultantExistsAfterCreate_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        ConsultantEntity entity = new ConsultantEntity();
        Optional<ConsultantEntity> optional = Optional.of(entity);

        ConsultantEntity result = ConsultantValidator.validateConsultantExistsAfterCreate(optional, id);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void validateConsultantExistsAfterCreate_ShouldThrowException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<ConsultantEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> ConsultantValidator.validateConsultantExistsAfterCreate(optional, id));
    }

    @Test
    void validateConsultantCheckExists_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        ConsultantEntity entity = new ConsultantEntity();
        Optional<ConsultantEntity> optional = Optional.of(entity);

        ConsultantEntity result = ConsultantValidator.validateConsultantCheckExists(optional, id);

        assertNotNull(result);
    }

    @Test
    void validateConsultantCheckExists_ShouldThrowException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<ConsultantEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> ConsultantValidator.validateConsultantCheckExists(optional, id));
    }

    @Test
    void validateCreationResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ConsultantValidator.validateCreationResult(true, UUID.randomUUID()));
    }

    @Test
    void validateCreationResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ConsultantValidator.validateCreationResult(false, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ConsultantValidator.validateDeleteResult(true, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ConsultantValidator.validateDeleteResult(false, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ConsultantValidator.validateUpdateResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ConsultantValidator.validateUpdateResult(false, UUID.randomUUID()));
    }

    @Test
    void validateConsultantNotExists_ShouldNotThrow_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<ConsultantEntity> optional = Optional.empty();

        assertDoesNotThrow(() -> ConsultantValidator.validateConsultantNotExists(optional, id));
    }

    @Test
    void validateConsultantNotExists_ShouldThrowValidationException_WhenPresent() {
        UUID id = UUID.randomUUID();
        Optional<ConsultantEntity> optional = Optional.of(new ConsultantEntity());

        assertThrows(ValidationException.class,
                () -> ConsultantValidator.validateConsultantNotExists(optional, id));
    }

    @Test
    void validateProfileIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        UUID id = UUID.randomUUID();
        ConsultantEntity entity = new ConsultantEntity();
        List<ConsultantEntity> list = List.of(entity);

        Optional<ConsultantEntity> result = ConsultantValidator.validateProfileIdSearchResult(list, id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validateProfileIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        UUID id = UUID.randomUUID();
        List<ConsultantEntity> list = List.of();

        Optional<ConsultantEntity> result = ConsultantValidator.validateProfileIdSearchResult(list, id);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateProfileIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        UUID id = UUID.randomUUID();
        List<ConsultantEntity> list = List.of(new ConsultantEntity(), new ConsultantEntity());

        assertThrows(EntityToManyResultException.class,
                () -> ConsultantValidator.validateProfileIdSearchResult(list, id));
    }

    @Test
    void validateIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        UUID id = UUID.randomUUID();
        ConsultantEntity entity = new ConsultantEntity();
        List<ConsultantEntity> list = List.of(entity);

        Optional<ConsultantEntity> result = ConsultantValidator.validateIdSearchResult(list, id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validateIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        UUID id = UUID.randomUUID();
        List<ConsultantEntity> list = List.of();

        Optional<ConsultantEntity> result = ConsultantValidator.validateIdSearchResult(list, id);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        UUID id = UUID.randomUUID();
        List<ConsultantEntity> list = List.of(new ConsultantEntity(), new ConsultantEntity());

        assertThrows(EntityToManyResultException.class,
                () -> ConsultantValidator.validateIdSearchResult(list, id));
    }
}