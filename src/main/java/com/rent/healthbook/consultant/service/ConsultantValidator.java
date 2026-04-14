package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ConsultantValidator {

    public static ConsultantEntity validateConsultantExistsAfterCreate(
            Optional<ConsultantEntity> consultant, UUID id) {
        return consultant.orElseThrow(() -> new EntityNotFoundInternalException(
                String.format("Consultant not found after create by profile id = %s", id)));
    }


    public static ConsultantEntity validateConsultantCheckExists(
            Optional<ConsultantEntity> consultant, UUID id) {
        return consultant.orElseThrow(() -> new EntityNotFoundInternalException(
                String.format("Consultant not found by id = %s", id)));
    }


    public static void validateCreationResult(boolean createResult, UUID id) {
        if (!createResult) {
            throw new ValidationException(
                    String.format("Consultant creation failed by profile id = %s", id));
        }
    }

    public static void validateDeleteResult(boolean deleteResult, UUID id) {
        if (!deleteResult) {
            throw new ValidationException(
                    String.format("Consultant delete by id = %s failed", id));
        }
    }

    public static void validateUpdateResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Consultant update online status by id = %s failed", id));
        }
    }

    public static void validateConsultantNotExists(
            Optional<ConsultantEntity> existingConsultant, UUID id) {
        if (existingConsultant.isPresent()) {
            throw new ValidationException(
                    String.format("Consultant by profile id = %s already exist", id));
        }
    }

    public static Optional<ConsultantEntity> validateProfileIdSearchResult(
            List<ConsultantEntity> consultant, UUID id) {
        if (consultant.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one consultant found by profile id = %s", id));
        }
        if (consultant.isEmpty()) {
            return Optional.empty();
        }
        return consultant.stream().findFirst();
    }

    public static Optional<ConsultantEntity> validateIdSearchResult(
            List<ConsultantEntity> consultant, UUID id) {
        if (consultant.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one consultant found by id = %s", id));
        }
        if (consultant.isEmpty()) {
            return Optional.empty();
        }
        return consultant.stream().findFirst();
    }
}
