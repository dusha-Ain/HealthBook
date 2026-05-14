package com.rent.healthbook.profile.service;


import com.rent.healthbook.profile.dao.ProfileDao;
import com.rent.healthbook.profile.dto.*;
import com.rent.healthbook.profile.dto.enums.ProfileRole;
import com.rent.healthbook.profile.entity.ProfileEntity;
import com.rent.healthbook.web.helpers.AppConfig;
import com.rent.healthbook.web.helpers.Pageable;
import com.rent.healthbook.web.exception.EntityNotFoundException;
import com.rent.healthbook.web.kafka.ProfileEventProducer;
import com.rent.healthbook.web.security.SecurityUtils;
import com.rent.healthbook.web.security.dto.JwtAuthentication;
import com.rent.healthbook.web.security.dto.ProfileCredential;
import com.rent.healthbook.web.security.dto.RefreshToken;
import com.rent.healthbook.web.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private final ProfileDao profileDao;
    private final ProfileMapper mapper;
    private final ProfileEventProducer eventProducer;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(ProfileDao profileDao,
                          ProfileMapper mapper,
                          ProfileEventProducer eventProducer,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.profileDao = profileDao;
        this.mapper = mapper;
        this.eventProducer = eventProducer;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtAuthentication signIn(ProfileCredential profileCredential){
        ProfileEntity profile = findByCredentials(profileCredential);
        return jwtService.generateAuthToken(
                profile.getEmail(),
                profile.getProfileRole().name(),
                profile.getId());
    }

    public JwtAuthentication refreshToken(RefreshToken refreshTokenDto){
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateToken(refreshToken)){
            ProfileEntity profile = getByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(
                    profile.getEmail(),
                    profile.getProfileRole().name(),
                    profile.getId(),
                    refreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
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
        eventProducer.sendProfileCreated(mapper.toProfileKafka(savedProfile));
        return mapper.toDomainProfile(savedProfile);
    }


    public Profile getById(UUID id) {
        ProfileEntity findProfile = ProfileValidator.validateProfileCheckExists(profileDao.getById(id), id);
        return mapper.toDomainProfile(findProfile);
    }

    public ProfileKafka getByIdForKafka(UUID id) {
        var profileEntity = ProfileValidator.validateProfileCheckExists(profileDao.getById(id), id);
        return mapper.toProfileKafka(profileEntity);
    }

    public boolean existsByIdForKafka(UUID id) {
        return profileDao.existsById(id);
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
        eventProducer.sendProfileDeleted(id);
        ProfileValidator.validateDeleteResult(profileDao.delete(id), id);
    }


    @Transactional
    public Profile updateProfile(UUID id, ProfileUpdate profile) {
        ProfileEntity findProfile =
                ProfileValidator.validateProfileCheckExists(profileDao.getByIdWithLock(id), id);
        SecurityUtils.checkAccessDenied(ProfileRole.USER.name(), id);
        var updateProfile = mapper.toEntity(findProfile, profile);
        ProfileValidator.validateUpdateResult(profileDao.update(updateProfile), id);
        ProfileEntity savedProfile =
                ProfileValidator.validateProfileExistsAfterUpdate(profileDao.getById(id), id);
        eventProducer.sendProfileUpdated(mapper.toProfileKafka(savedProfile));
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

    public ProfileEntity findByCredentials(ProfileCredential profileCredential){
        Optional<ProfileEntity> OptionalProfile = profileDao.getProfileByEmail(profileCredential.getEmail());
        if (OptionalProfile.isPresent()){
            ProfileEntity profile = OptionalProfile.get();
            if (passwordEncoder.matches(profileCredential.getPassword(), profile.getPassword())){
                return profile;
            }
        }
        throw new RuntimeException("Email or password is not correct");
    }

    public ProfileEntity getByEmail(String email){
        return profileDao.getProfileByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("Not found profile by email"));
    }

}
