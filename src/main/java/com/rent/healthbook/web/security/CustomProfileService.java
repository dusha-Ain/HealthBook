package com.rent.healthbook.web.security;

import com.rent.healthbook.profile.dao.ProfileDao;
import com.rent.healthbook.web.security.dto.CustomProfileDetails;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomProfileService implements UserDetailsService {
    private final ProfileDao profileDao;

    public CustomProfileService(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @Override
    public CustomProfileDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return profileDao.getProfileByEmail(email)
                .map(CustomProfileDetails::new).orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
