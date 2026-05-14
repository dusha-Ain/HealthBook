package com.rent.healthbook.doctor.service;

import com.rent.healthbook.doctor.dao.DoctorDao;
import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.dto.Doctors;
import com.rent.healthbook.doctor.entity.DoctorEntity;
import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.web.helpers.AppConfig;
import com.rent.healthbook.web.helpers.Pageable;
import com.rent.healthbook.web.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class DoctorService {

    private final DoctorMapper mapper;
    private final DoctorDao doctorDao;

    public DoctorService(DoctorMapper mapper,
                         DoctorDao doctorDao) {
        this.mapper = mapper;
        this.doctorDao = doctorDao;
    }

    @Transactional
    public Doctor addDoctor(@Valid Doctor doctor) {
        var maybeDoctor = doctorDao.getDoctorByProfileIdWithLock(doctor.idProfile());
        DoctorValidator.validateDoctorNotExists(maybeDoctor, doctor.idProfile());
        DoctorValidator.validateStartEndDate(doctor.workStart(), doctor.workEnd());
        var doctorEntity = mapper.toEntity(doctor);
        DoctorValidator.validateCreationResult(
                doctorDao.createDoctor(doctorEntity),
                doctor.idProfile());
        DoctorEntity doctorCreated =
                DoctorValidator.validateDoctorExistsAfterCreate(
                        doctorDao.getDoctorByProfileId(doctorEntity.getIdProfile()),
                        doctor.idProfile());
        return mapper.toDomainDoctor(doctorCreated);
    }

    public Doctor getDoctorById(UUID id) {
        DoctorEntity findDoctor =
                DoctorValidator.validateDoctorCheckExists(doctorDao.getDoctorById(id), id);
        return mapper.toDomainDoctor(findDoctor);
    }

    public Doctors getAllDoctorsByFilter(Pageable filter) {
        filter.setPageSize(Objects.requireNonNullElse(filter.getPageSize(), AppConfig.getDefaultPageSize()));
        filter.setPageNumber(Objects.requireNonNullElse(filter.getPageNumber(), AppConfig.getDefaultPageNumber()));
        var doctorsEntity = doctorDao.getAllByFilter(filter);
        return new Doctors(doctorsEntity.list.stream()
                .map(mapper::toDomainDoctor).toList());
    }

    @Transactional
    public Doctor updateDoctor(UUID id, @Valid DoctorUpdate doctor) {
        DoctorEntity findDoctor =
                DoctorValidator.validateDoctorCheckExists(doctorDao.getDoctorByIdWithLock(id), id);
        SecurityUtils.checkAccessDenied(ProfileRole.DOCTOR.name(), findDoctor.getIdProfile());
        DoctorValidator.validateStartEndDate(doctor.workStart(), doctor.workEnd());
        var updateDoctor = mapper.toEntity(findDoctor, doctor);
        DoctorValidator.validateUpdateResult(doctorDao.updateDoctor(updateDoctor), id);
        DoctorEntity savedDoctor =
                DoctorValidator.validateDoctorExistsAfterUpdate(doctorDao.getDoctorById(id), id);
        return mapper.toDomainDoctor(savedDoctor);
    }

    @Transactional
    public void deleteDoctor(UUID id) {
        DoctorValidator.validateDoctorCheckExists(doctorDao.getDoctorByIdWithLock(id), id);
        DoctorValidator.validateDeleteResult(doctorDao.deleteDoctor(id), id);
    }
}
