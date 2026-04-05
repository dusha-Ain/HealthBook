package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.web.exception.EntityNotFoundInternalException;
import com.rent.healthbook.web.exception.EntityToManyResultException;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DoctorValidator {

    public DoctorEntity validateDoctorExistsAfterCreate(
            Optional<DoctorEntity> doctor, UUID id) {
        return doctor.orElseThrow(() -> new EntityNotFoundInternalException(
                String.format("Doctor not found after create by profile id = %s", id)));
    }

    public DoctorEntity validateDoctorExistsAfterUpdate(
            Optional<DoctorEntity> doctor, UUID id) {
        return doctor.orElseThrow(() -> new EntityNotFoundInternalException(
                String.format("Doctor not found after update by id = %s", id)));
    }

    public DoctorEntity validateDoctorCheckExists(
            Optional<DoctorEntity> doctor, UUID id) {
        return doctor.orElseThrow(() -> new ValidationException(
                String.format("Doctor not found by id = %s", id)));
    }

    public void validateCreationResult(boolean createResult, UUID id) {
        if (!createResult) {
            throw new ValidationException(
                    String.format("Doctor creation failed by profile id = %s", id));
        }
    }

    public void validateUpdateResult(boolean updateResult, UUID id) {
        if (!updateResult) {
            throw new ValidationException(
                    String.format("Doctor update failed by id = %s", id));
        }
    }

    public void validateDeleteResult(boolean deleteResult, UUID id) {
        if (!deleteResult) {
            throw new ValidationException(
                    String.format("Doctor delete failed by id = %s", id));
        }
    }

    public void validateDoctorNotExists(
            Optional<DoctorEntity> existingDoctor, UUID id) {
        if (existingDoctor.isPresent()) {
            throw new ValidationException(
                    String.format("Doctor by profile id = %s already exist", id));
        }
    }

    public void validateStartEndDate(Date start, Date end){
        if (end == null) return;
        if (start.after(end)) throw new ValidationException("Start date should be after end date");
    }


    public Optional<DoctorEntity> validateProfileIdSearchResult(
            List<DoctorEntity> doctor, UUID id) {
        if (doctor.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one doctor found by profile id = %s", id));
        }
        if (doctor.isEmpty()) {
            return Optional.empty();
        }
        return doctor.stream().findFirst();
    }

    public Optional<DoctorEntity> validateIdSearchResult(
            List<DoctorEntity> doctor, UUID id) {
        if (doctor.size() > 1) {
            throw new EntityToManyResultException(
                    String.format("More than one doctor found by id = %s", id));
        }
        if (doctor.isEmpty()) {
            return Optional.empty();
        }
        return doctor.stream().findFirst();
    }

}
