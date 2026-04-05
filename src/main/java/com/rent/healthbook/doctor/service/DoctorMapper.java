package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.web.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DoctorMapper {

    public DoctorEntity toEntity(Doctor doctor){
        if (doctor == null)
            throw new ValidationException("Doctor should be not null");
        return new DoctorEntity(
          doctor.id(),
          doctor.specialization(),
          doctor.cabinetNumber(),
          doctor.workStart(),
          doctor.workEnd(),
          doctor.durationReception(),
          doctor.idProfile()
        );
    }

    public Doctor toDomainDoctor(DoctorEntity doctor){
        if (doctor == null)
            throw new ValidationException("Doctor should be not null");
        return new Doctor(
                doctor.getId(),
                doctor.getSpecialization(),
                doctor.getCabinetNumber(),
                doctor.getWorkStart(),
                doctor.getWorkEnd(),
                doctor.getDurationReception(),
                doctor.getIdProfile()
        );
    }

    public DoctorEntity toEntity(DoctorEntity doctorEntity, DoctorUpdate doctor){
        if (doctor == null)
            return doctorEntity;
        Optional.ofNullable(doctor.specialization()).ifPresent(doctorEntity::setSpecialization);
        Optional.ofNullable(doctor.cabinetNumber()).ifPresent(doctorEntity::setCabinetNumber);
        Optional.ofNullable(doctor.workStart()).ifPresent(doctorEntity::setWorkStart);
        Optional.ofNullable(doctor.workEnd()).ifPresent(doctorEntity::setWorkEnd);
        Optional.ofNullable(doctor.durationReception()).ifPresent(doctorEntity::setDurationReception);
        Optional.ofNullable(doctor.idProfile()).ifPresent(doctorEntity::setIdProfile);
        return doctorEntity;
    }
}
