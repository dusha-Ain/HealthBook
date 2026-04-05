package com.rent.healthbook.profile.service;

import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.web.exception.BadCredentialsPasswordException;
import com.rent.healthbook.web.exception.EntityNotFoundException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileValidator {

    public Optional<ProfileEntity> validatePhoneSearchResult(List<ProfileEntity> profiles, String phone) {
        if (profiles.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one profile found by phone = %s", phone));
        }
        if (profiles.isEmpty()) {
            return Optional.empty();
        }
        return profiles.stream().findFirst();
    }

    public Optional<ProfileEntity> validateIdSearchResult(List<ProfileEntity> profiles, UUID id) {
        if (profiles.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one profile found by id = %s", id));
        }
        if (profiles.isEmpty()) {
            return Optional.empty();
        }
        return profiles.stream().findFirst();
    }

    public ProfileEntity validateProfileExistsAfterCreate(Optional<ProfileEntity> profile, String phone) {
        return profile.orElseThrow(() -> new ValidationException(
                String.format("Profile not found after create by phone = %s", phone)));
    }

    public ProfileEntity validateProfileExistsAfterUpdate(Optional<ProfileEntity> profile, UUID id) {
        return profile.orElseThrow(() -> new ValidationException(
                String.format("Profile by id = %s not found after update", id)));
    }

    public ProfileEntity validateProfileCheckExists(Optional<ProfileEntity> profile, UUID id) {
        return profile.orElseThrow(() -> new EntityNotFoundException(
                String.format("Profile not found by id = %s", id)));
    }

    public void validateOldPassword(String currentPassword, String storedPassword) {
        if (!storedPassword.equals(currentPassword)) {
            throw new BadCredentialsPasswordException("Wrong old password");
        }
    }

    public void validateConfirmPassword(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new ValidationException("Passwords don't match");
        }
    }

    public void validateCreationResult(boolean createResult, String phone) {
        if (!createResult) {
            throw new ValidationException(
                    String.format("Profile creation failed for phone = %s", phone));
        }
    }

    public void validateUpdateResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Profile update by id = %s failed", id));
        }
    }

    public void validateUpdatePasswordResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Profile password update by id = %s failed", id));
        }
    }

    public void validateDeleteResult(boolean deleteResult, UUID id) {
        if (!deleteResult) {
            throw new ValidationException(
                    String.format("Profile delete by id = %s failed", id));
        }
    }

    public void validateProfileNotExists(Optional<ProfileEntity> existingProfile, String phone) {
        if (existingProfile.isPresent()) {
            throw new ValidationException(
                    String.format("Profile by phone = %s already exist", phone));
        }
    }

    public void validateBlockResult(boolean block, UUID id) {
        if (!block) {
            throw new ValidationException(
                    String.format("Profile block by id = %s failed", id));
        }
    }
}
