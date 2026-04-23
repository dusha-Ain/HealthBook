package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorValidatorTest {

    @Test
    void validateDoctorExistsAfterCreate_ShouldReturnEntity_WhenPresent() {
        UUID id =UUID.randomUUID();
        DoctorEntity entity=new DoctorEntity();
        Optional<DoctorEntity>optional=Optional.of(entity);
        DoctorEntity result =DoctorValidator.validateDoctorExistsAfterCreate(optional, id);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void validateDoctorExistsAfterCreate_ShouldThrowException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<DoctorEntity> optional=Optional.empty();
        assertThrows(EntityNotFoundInternalException.class,
                ()->DoctorValidator.validateDoctorExistsAfterUpdate(optional,id));

    }

    @Test
    void validateDoctorExistsAfterUpdate_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        Optional<DoctorEntity> optional = Optional.of(entity);

        DoctorEntity result = DoctorValidator.validateDoctorExistsAfterUpdate(optional, id);

        assertNotNull(result);
    }

    @Test
    void validateDoctorExistsAfterUpdate_ShouldThrowException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<DoctorEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> DoctorValidator.validateDoctorExistsAfterUpdate(optional, id));
    }

    @Test
    void validateDoctorCheckExists_ShouldReturnEntity_WhenPresent() {
        UUID id = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        Optional<DoctorEntity> optional = Optional.of(entity);

        DoctorEntity result = DoctorValidator.validateDoctorCheckExists(optional, id);

        assertNotNull(result);
    }

    @Test
    void validateDoctorCheckExists_ShouldThrowValidationException_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<DoctorEntity> optional = Optional.empty();

        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateDoctorCheckExists(optional, id));
    }

    @Test
    void validateCreationResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> DoctorValidator.validateCreationResult(true, UUID.randomUUID()));
    }

    @Test
    void validateCreationResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateCreationResult(false, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> DoctorValidator.validateUpdateResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateUpdateResult(false, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> DoctorValidator.validateDeleteResult(true, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateDeleteResult(false, UUID.randomUUID()));
    }

    @Test
    void validateDoctorNotExists_ShouldNotThrow_WhenEmpty() {
        UUID id = UUID.randomUUID();
        Optional<DoctorEntity> optional = Optional.empty();

        assertDoesNotThrow(() -> DoctorValidator.validateDoctorNotExists(optional, id));
    }

    @Test
    void validateDoctorNotExists_ShouldThrowValidationException_WhenPresent() {
        UUID id = UUID.randomUUID();
        Optional<DoctorEntity> optional = Optional.of(new DoctorEntity());

        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateDoctorNotExists(optional, id));
    }

    @Test
    void validateStartEndDate_ShouldNotThrow_WhenStartIsBeforeEnd() {
        Date start = Date.valueOf("2024-01-01");
        Date end = Date.valueOf("2024-12-31");

        assertDoesNotThrow(() -> DoctorValidator.validateStartEndDate(start, end));
    }

    @Test
    void validateStartEndDate_ShouldNotThrow_WhenEndIsNull() {
        Date start = Date.valueOf("2024-01-01");

        assertDoesNotThrow(() -> DoctorValidator.validateStartEndDate(start, null));
    }

    @Test
    void validateStartEndDate_ShouldThrowValidationException_WhenStartIsAfterEnd() {
        Date start = Date.valueOf("2024-12-31");
        Date end = Date.valueOf("2024-01-01");

        assertThrows(ValidationException.class,
                () -> DoctorValidator.validateStartEndDate(start, end));
    }

    @Test
    void validateProfileIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        UUID id = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        List<DoctorEntity> list = List.of(entity);

        Optional<DoctorEntity> result = DoctorValidator.validateProfileIdSearchResult(list, id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validateProfileIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        UUID id = UUID.randomUUID();
        List<DoctorEntity> list = List.of();

        Optional<DoctorEntity> result = DoctorValidator.validateProfileIdSearchResult(list, id);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateProfileIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        UUID id = UUID.randomUUID();
        List<DoctorEntity> list = List.of(new DoctorEntity(), new DoctorEntity());

        assertThrows(EntityToManyResultException.class,
                () -> DoctorValidator.validateProfileIdSearchResult(list, id));
    }

    @Test
    void validateIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        UUID id = UUID.randomUUID();
        DoctorEntity entity = new DoctorEntity();
        List<DoctorEntity> list = List.of(entity);

        Optional<DoctorEntity> result = DoctorValidator.validateIdSearchResult(list, id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validateIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        UUID id = UUID.randomUUID();
        List<DoctorEntity> list = List.of();

        Optional<DoctorEntity> result = DoctorValidator.validateIdSearchResult(list, id);

        assertTrue(result.isEmpty());
    }

    @Test
    void validateIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        UUID id = UUID.randomUUID();
        List<DoctorEntity> list = List.of(new DoctorEntity(), new DoctorEntity());

        assertThrows(EntityToManyResultException.class,
                () -> DoctorValidator.validateIdSearchResult(list, id));
    }
}