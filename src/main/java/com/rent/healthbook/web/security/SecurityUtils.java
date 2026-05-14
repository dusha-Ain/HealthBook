package com.rent.healthbook.web.security;

import com.rent.healthbook.web.security.dto.CustomProfileDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class SecurityUtils {

    private static CustomProfileDetails getCurrentProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CustomProfileDetails) Objects.requireNonNull(auth).getPrincipal();
    }

    public static void checkAccessDenied(String role, UUID idProfile){
        CustomProfileDetails currentProfile = SecurityUtils.getCurrentProfile();
        String currentRole = currentProfile.profile().getProfileRole().name();
        UUID currentProfileId = currentProfile.profile().getId();
        if (currentRole.equals(role)) {
            if (!currentProfileId.equals(idProfile)) {
                throw new AccessDeniedException(currentRole +" can only edit their own profile");
            }
        }
    }

}
