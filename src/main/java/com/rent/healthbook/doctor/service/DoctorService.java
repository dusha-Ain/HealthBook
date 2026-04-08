package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.dao.DoctorDao;
import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.dto.Doctors;
import com.rent.healthbook.doctor.entity.DoctorEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorMapper mapper;
    private final DoctorDao doctorDao;
    private final DoctorValidator doctorValidator;

    public DoctorService(DoctorMapper mapper,
                         DoctorDao doctorDao,
                         DoctorValidator doctorValidator) {
        this.mapper = mapper;
        this.doctorDao = doctorDao;
        this.doctorValidator = doctorValidator;
    }

    public Doctor addDoctor(@Valid Doctor doctor) {
        var maybeDoctor = doctorDao.getDoctorByProfileIdWithLock(doctor.idProfile());
        doctorValidator.validateDoctorNotExists(maybeDoctor, doctor.idProfile());
        doctorValidator.validateStartEndDate(doctor.workStart(), doctor.workEnd());
        var doctorEntity = mapper.toEntity(doctor);
        doctorValidator.validateCreationResult(
                doctorDao.createDoctor(doctorEntity),
                doctor.idProfile());
        DoctorEntity doctorCreated =
                doctorValidator.validateDoctorExistsAfterCreate(
                        doctorDao.getDoctorByProfileId(doctorEntity.getIdProfile()),
                        doctor.idProfile());
        return mapper.toDomainDoctor(doctorCreated);
    }

    public Doctor getDoctorById(UUID id) {
        DoctorEntity findDoctor =
                doctorValidator.validateDoctorCheckExists(doctorDao.getDoctorById(id), id);
        return mapper.toDomainDoctor(findDoctor);
    }

    public Doctors getAllDoctors() {
        var doctorsEntity = doctorDao.getAll();
        return new Doctors(doctorsEntity.list.stream()
                .map(mapper::toDomainDoctor).toList());
    }

    public Doctor updateDoctor(UUID id, @Valid DoctorUpdate doctor) {
        DoctorEntity findDoctor =
                doctorValidator.validateDoctorCheckExists(doctorDao.getDoctorByIdWithLock(id), id);
        doctorValidator.validateStartEndDate(doctor.workStart(), doctor.workEnd());
        var updateDoctor = mapper.toEntity(findDoctor, doctor);
        doctorValidator.validateUpdateResult(doctorDao.updateDoctor(updateDoctor), id);
        DoctorEntity savedDoctor =
                doctorValidator.validateDoctorExistsAfterUpdate(doctorDao.getDoctorById(id), id);
        return mapper.toDomainDoctor(savedDoctor);
    }

    public void deleteDoctor(UUID id) {
        doctorValidator.validateDoctorCheckExists(doctorDao.getDoctorByIdWithLock(id), id);
        doctorValidator.validateDeleteResult(doctorDao.deleteDoctor(id), id);
    }
}
