package com.rent.healthbook.profile.service;


import com.rent.healthbook.profile.dao.ProfileDao;
import com.rent.healthbook.profile.dto.*;
import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.web.AppConfig;
import com.rent.healthbook.web.Pageable;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileDao profileDao;
    private final ProfileMapper mapper;

    public ProfileService(ProfileDao profileDao,
                          ProfileMapper mapper) {
        this.profileDao = profileDao;
        this.mapper = mapper;
    }

    @Transactional
    public Profile addProfile(ProfileSecurity profile) {
        Optional<ProfileEntity> maybeGuest =
                profileDao.getProfileByPhoneWithLock(profile.phone());
        ProfileValidator.validateProfileNotExists(maybeGuest, profile.phone());
        var profileEntity = mapper.toEntity(profile);
        ProfileValidator.validateCreationResult(
                profileDao.createProfile(profileEntity),
                profile.phone());
        ProfileEntity savedProfile = ProfileValidator.validateProfileExistsAfterCreate(
                profileDao.getProfileByPhone(profile.phone()),
                profile.phone());
        return mapper.toDomainProfile(savedProfile);
    }


    public Profile getById(UUID id) {
        ProfileEntity findProfile = ProfileValidator.validateProfileCheckExists(profileDao.getById(id), id);
        return mapper.toDomainProfile(findProfile);
    }


    public Profiles getAllProfilesByFilter(ProfileSearchFilter filter) {
        var pageable = new Pageable();
        pageable.setPageSize(Objects.requireNonNullElse(filter.pageSize(), AppConfig.getDefaultPageSize()));
        pageable.setPageNumber(Objects.requireNonNullElse(filter.pageNumber(), AppConfig.getDefaultPageNumber()));
        var profilesEntity = profileDao.getAllByFilter(filter, pageable);
        return new Profiles(profilesEntity.list.stream()
                .map(mapper::toDomainProfile).toList());
    }


    @Transactional
    public void deleteProfile(UUID id) {
        ProfileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        ProfileValidator.validateDeleteResult(profileDao.delete(id), id);
    }


    @Transactional
    public Profile updateProfile(UUID id, ProfileUpdate profile) {
        ProfileEntity findProfile =
                ProfileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        var updateProfile = mapper.toEntity(findProfile, profile);
        ProfileValidator.validateUpdateResult(profileDao.update(updateProfile), id);
        ProfileEntity savedProfile =
                ProfileValidator.validateProfileExistsAfterUpdate(profileDao.getById(id), id);
        return mapper.toDomainProfile(savedProfile);
    }


    @Transactional
    public void updateProfilePassword(UUID id, @Valid ProfilePassword password) {
        var maybeProfile =
                ProfileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        ProfileValidator.validateOldPassword(maybeProfile.getPassword(), password.currentPassword());
        ProfileValidator.validateConfirmPassword(password.newPassword(), password.confirmPassword());
        ProfileValidator.validateUpdatePasswordResult(
                profileDao.updatePassword(id, password.newPassword()), id);
    }

    public void blockProfile(UUID id) {
        ProfileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        ProfileValidator.validateBlockResult(profileDao.block(id), id);
    }
}
