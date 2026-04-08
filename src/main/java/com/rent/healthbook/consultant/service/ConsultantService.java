package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.dao.ConsultantDao;
import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.dto.Consultants;
import com.rent.healthbook.consultant.entity.ConsultantEntity;
import com.rent.healthbook.profile.dto.Profiles;
import com.rent.healthbook.profile.entity.ProfileEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ConsultantService {

    private final ConsultantMapper mapper;
    private final ConsultantDao consultantDao;
    private final ConsultantValidator consultantValidator;

    public ConsultantService(ConsultantMapper consultantMapper,
                             ConsultantDao consultantDao,
                             ConsultantValidator consultantValidator) {
        this.mapper = consultantMapper;
        this.consultantDao = consultantDao;
        this.consultantValidator = consultantValidator;
    }


    @Transactional
    public Consultant addConsultant(@Valid Consultant consultant) {
        var maybeConsultant = consultantDao.getConsultantByProfileIdWithLock(consultant.idProfile());
        consultantValidator.validateConsultantNotExists(maybeConsultant, consultant.idProfile());
        var consultantEntity = mapper.toEntity(consultant);
        consultantValidator.validateCreationResult(
                consultantDao.createConsultant(consultantEntity),
                consultant.idProfile());
        ConsultantEntity consultantCreated =
                consultantValidator.validateConsultantExistsAfterCreate(
                        consultantDao.getConsultantByProfileId(consultantEntity.getIdProfile()),
                        consultant.idProfile());
        return mapper.toDomainConsultant(consultantCreated);
    }

    public Consultant getById(UUID id) {
        ConsultantEntity findConsultant =
                consultantValidator.validateConsultantCheckExists(consultantDao.getConsultantById(id), id);
        return mapper.toDomainConsultant(findConsultant);
    }

    public Consultants getAllConsultants() {
        var consultantsEntity = consultantDao.getAll();
        return new Consultants(consultantsEntity.list.stream()
                .map(mapper::toDomainConsultant).toList());
    }

    @Transactional
    public void updateConsultantOnline(UUID id, Boolean online) {
        consultantValidator.validateConsultantCheckExists(consultantDao.getConsultantByIdWithLock(id), id);
        consultantValidator.validateUpdateResult(consultantDao.updateOnlineStatus(online, id), id);
    }

    @Transactional
    public void deleteConsultant(UUID id) {
        consultantValidator.validateConsultantCheckExists(consultantDao.getConsultantByIdWithLock(id), id);
        consultantValidator.validateDeleteResult(consultantDao.delete(id), id);
    }
}
