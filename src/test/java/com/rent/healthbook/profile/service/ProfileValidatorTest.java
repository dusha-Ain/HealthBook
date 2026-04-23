package com.rent.healthbook.profile.service;

import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.web.exception.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProfileValidatorTest {

    @Test
    void validatePhoneSearchResult_ShouldReturnOptional_WhenSingleResult() {
        ProfileEntity entity = new ProfileEntity();
        List<ProfileEntity> list = List.of(entity);

        Optional<ProfileEntity> result = ProfileValidator.validatePhoneSearchResult(list, "+79255272450");

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void validatePhoneSearchResult_ShouldReturnEmpty_WhenNoResults() {
        List<ProfileEntity> list = List.of();

        Optional<ProfileEntity> result = ProfileValidator.validatePhoneSearchResult(list, "+79255272450");

        assertTrue(result.isEmpty());
    }

    @Test
    void validatePhoneSearchResult_ShouldThrowException_WhenMultipleResults() {
        List<ProfileEntity> list = List.of(new ProfileEntity(), new ProfileEntity());

        assertThrows(EntityToManyResultException.class,
                () -> ProfileValidator.validatePhoneSearchResult(list, "+79255272450"));
    }

    @Test
    void validateIdSearchResult_ShouldReturnOptional_WhenSingleResult() {
        ProfileEntity entity = new ProfileEntity();
        List<ProfileEntity> list = List.of(entity);

        Optional<ProfileEntity> result = ProfileValidator.validateIdSearchResult(list, UUID.randomUUID());

        assertTrue(result.isPresent());
    }

    @Test
    void validateIdSearchResult_ShouldReturnEmpty_WhenNoResults() {
        List<ProfileEntity> list = List.of();

        Optional<ProfileEntity> result = ProfileValidator.validateIdSearchResult(list, UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    void validateIdSearchResult_ShouldThrowException_WhenMultipleResults() {
        List<ProfileEntity> list = List.of(new ProfileEntity(), new ProfileEntity());

        assertThrows(EntityToManyResultException.class,
                () -> ProfileValidator.validateIdSearchResult(list, UUID.randomUUID()));
    }

    @Test
    void validateProfileExistsAfterCreate_ShouldReturnEntity_WhenPresent() {
        ProfileEntity entity = new ProfileEntity();
        Optional<ProfileEntity> optional = Optional.of(entity);

        ProfileEntity result = ProfileValidator.validateProfileExistsAfterCreate(optional, "+79255272450");

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void validateProfileExistsAfterCreate_ShouldThrowException_WhenEmpty() {
        Optional<ProfileEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> ProfileValidator.validateProfileExistsAfterCreate(optional, "+79255272450"));
    }

    @Test
    void validateProfileExistsAfterUpdate_ShouldReturnEntity_WhenPresent() {
        ProfileEntity entity = new ProfileEntity();
        Optional<ProfileEntity> optional = Optional.of(entity);

        ProfileEntity result = ProfileValidator.validateProfileExistsAfterUpdate(optional, UUID.randomUUID());

        assertNotNull(result);
    }

    @Test
    void validateProfileExistsAfterUpdate_ShouldThrowException_WhenEmpty() {
        Optional<ProfileEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundInternalException.class,
                () -> ProfileValidator.validateProfileExistsAfterUpdate(optional, UUID.randomUUID()));
    }

    @Test
    void validateProfileCheckExists_ShouldReturnEntity_WhenPresent() {
        ProfileEntity entity = new ProfileEntity();
        Optional<ProfileEntity> optional = Optional.of(entity);

        ProfileEntity result = ProfileValidator.validateProfileCheckExists(optional, UUID.randomUUID());

        assertNotNull(result);
    }

    @Test
    void validateProfileCheckExists_ShouldThrowEntityNotFoundException_WhenEmpty() {
        Optional<ProfileEntity> optional = Optional.empty();

        assertThrows(EntityNotFoundException.class,
                () -> ProfileValidator.validateProfileCheckExists(optional, UUID.randomUUID()));
    }

    @Test
    void validateOldPassword_ShouldNotThrow_WhenPasswordsMatch() {
        assertDoesNotThrow(() -> ProfileValidator.validateOldPassword("storedPass", "storedPass"));
    }

    @Test
    void validateOldPassword_ShouldThrowBadCredentialsPasswordException_WhenPasswordsDontMatch() {
        assertThrows(BadCredentialsPasswordException.class,
                () -> ProfileValidator.validateOldPassword("storedPass", "wrongPass"));
    }

    @Test
    void validateConfirmPassword_ShouldNotThrow_WhenPasswordsMatch() {
        assertDoesNotThrow(() -> ProfileValidator.validateConfirmPassword("newPass", "newPass"));
    }

    @Test
    void validateConfirmPassword_ShouldThrowValidationException_WhenPasswordsDontMatch() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateConfirmPassword("newPass", "differentPass"));
    }

    @Test
    void validateCreationResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ProfileValidator.validateCreationResult(true, "+79255272450"));
    }

    @Test
    void validateCreationResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateCreationResult(false, "+79255272450"));
    }

    @Test
    void validateUpdateResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ProfileValidator.validateUpdateResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdateResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateUpdateResult(false, UUID.randomUUID()));
    }

    @Test
    void validateUpdatePasswordResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ProfileValidator.validateUpdatePasswordResult(true, UUID.randomUUID()));
    }

    @Test
    void validateUpdatePasswordResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateUpdatePasswordResult(false, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ProfileValidator.validateDeleteResult(true, UUID.randomUUID()));
    }

    @Test
    void validateDeleteResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateDeleteResult(false, UUID.randomUUID()));
    }

    @Test
    void validateProfileNotExists_ShouldNotThrow_WhenEmpty() {
        Optional<ProfileEntity> optional = Optional.empty();

        assertDoesNotThrow(() -> ProfileValidator.validateProfileNotExists(optional, "+79255272450"));
    }

    @Test
    void validateProfileNotExists_ShouldThrowValidationException_WhenPresent() {
        Optional<ProfileEntity> optional = Optional.of(new ProfileEntity());

        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateProfileNotExists(optional, "+79255272450"));
    }

    @Test
    void validateBlockResult_ShouldNotThrow_WhenTrue() {
        assertDoesNotThrow(() -> ProfileValidator.validateBlockResult(true, UUID.randomUUID()));
    }

    @Test
    void validateBlockResult_ShouldThrowValidationException_WhenFalse() {
        assertThrows(ValidationException.class,
                () -> ProfileValidator.validateBlockResult(false, UUID.randomUUID()));
    }
}