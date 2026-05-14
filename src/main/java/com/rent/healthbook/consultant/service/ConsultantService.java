package com.rent.healthbook.consultant.service;

import com.rent.healthbook.consultant.dao.ConsultantDao;
import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.dto.Consultants;
import com.rent.healthbook.consultant.entity.ConsultantEntity;
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
public class ConsultantService {

    private final ConsultantMapper mapper;
    private final ConsultantDao consultantDao;

    public ConsultantService(ConsultantMapper consultantMapper,
                             ConsultantDao consultantDao) {
        this.mapper = consultantMapper;
        this.consultantDao = consultantDao;
    }


    @Transactional
    public Consultant addConsultant(@Valid Consultant consultant) {
        var maybeConsultant = consultantDao
                .getConsultantByProfileIdWithLock(consultant.idProfile());
        ConsultantValidator.validateConsultantNotExists(maybeConsultant, consultant.idProfile());
        var consultantEntity = mapper.toEntity(consultant);
        ConsultantValidator.validateCreationResult(
                consultantDao.createConsultant(consultantEntity),
                consultant.idProfile());
        ConsultantEntity consultantCreated =
                ConsultantValidator.validateConsultantExistsAfterCreate(
                        consultantDao.getConsultantByProfileId(consultantEntity.getIdProfile()),
                        consultant.idProfile());
        return mapper.toDomainConsultant(consultantCreated);
    }

    public Consultant getById(UUID id) {
        ConsultantEntity findConsultant =
                ConsultantValidator.validateConsultantCheckExists(consultantDao.getConsultantById(id), id);
        return mapper.toDomainConsultant(findConsultant);
    }

    public Consultants getAllConsultantsByFilter(Pageable filter) {
        filter.setPageSize(Objects.requireNonNullElse(filter.getPageSize(), AppConfig.getDefaultPageSize()));
        filter.setPageNumber(Objects.requireNonNullElse(filter.getPageNumber(), AppConfig.getDefaultPageNumber()));
        var consultantsEntity = consultantDao.getAllByFilter(filter);
        return new Consultants(consultantsEntity.list.stream()
                .map(mapper::toDomainConsultant).toList());
    }

    @Transactional
    public void updateConsultantOnline(UUID id, Boolean online) {
        var consul =
                ConsultantValidator.validateConsultantCheckExists(consultantDao.getConsultantByIdWithLock(id), id);
        SecurityUtils.checkAccessDenied(ProfileRole.CONSULTANT.name(), consul.getIdProfile());
        ConsultantValidator.validateUpdateResult(consultantDao.updateOnlineStatus(online, id), id);
    }

    @Transactional
    public void deleteConsultant(UUID id) {
        ConsultantValidator.validateConsultantCheckExists(consultantDao.getConsultantByIdWithLock(id), id);
        ConsultantValidator.validateDeleteResult(consultantDao.delete(id), id);
    }
}
