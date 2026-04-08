package com.rent.healthbook.profile.service;


import com.rent.healthbook.profile.dao.ProfileDao;
import com.rent.healthbook.profile.dto.*;
import com.rent.healthbook.profile.entity.ProfileEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileDao profileDao;
    private final ProfileMapper mapper;
    private final ProfileValidator profileValidator;

    public ProfileService(ProfileDao profileDao, ProfileMapper mapper, ProfileValidator profileValidator) {
        this.profileDao = profileDao;
        this.mapper = mapper;
        this.profileValidator = profileValidator;
    }

    @Transactional
    public Profile addProfile(ProfileSecurity profile) {
        Optional<ProfileEntity> maybeGuest =
                profileDao.getProfileByPhoneWithLock(profile.phone());
        profileValidator.validateProfileNotExists(maybeGuest, profile.phone());
        var profileEntity = mapper.toEntity(profile);
        profileValidator.validateCreationResult(
                profileDao.createProfile(profileEntity),
                profile.phone());
        ProfileEntity savedProfile = profileValidator.validateProfileExistsAfterCreate(
                profileDao.getProfileByPhone(profile.phone()),
                profile.phone());
        return mapper.toDomainProfile(savedProfile);
    }


    public Profile getById(UUID id) {
        ProfileEntity findProfile = profileValidator.validateProfileCheckExists(profileDao.getById(id), id);
        return mapper.toDomainProfile(findProfile);
    }


    public Profiles getAllProfiles() {
        var profilesEntity = profileDao.getAll();
        return new Profiles(profilesEntity.list.stream()
                .map(mapper::toDomainProfile).toList());
    }


    @Transactional
    public void deleteProfile(UUID id) {
        profileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        profileValidator.validateDeleteResult(profileDao.delete(id), id);
    }


    @Transactional
    public Profile updateProfile(UUID id, ProfileUpdate profile) {
        ProfileEntity findProfile =
                profileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        var updateProfile = mapper.toEntity(findProfile, profile);
        profileValidator.validateUpdateResult(profileDao.update(updateProfile), id);
        ProfileEntity savedProfile =
                profileValidator.validateProfileExistsAfterUpdate(profileDao.getById(id), id);
        return mapper.toDomainProfile(savedProfile);
    }


    @Transactional
    public void updateProfilePassword(UUID id, @Valid ProfilePassword password) {
        var maybeProfile =
                profileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        profileValidator.validateOldPassword(maybeProfile.getPassword(), password.currentPassword());
        profileValidator.validateConfirmPassword(password.newPassword(),password.confirmPassword());
        profileValidator.validateUpdatePasswordResult(
                profileDao.updatePassword(id, password.newPassword()), id);
    }

    public void blockProfile(UUID id) {
        profileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        profileValidator.validateBlockResult(profileDao.block(id), id);
    }
}
