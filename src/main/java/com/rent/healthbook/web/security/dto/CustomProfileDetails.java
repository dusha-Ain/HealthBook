package com.rent.healthbook.web.security.dto;

import com.rent.healthbook.profile.entity.ProfileEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record CustomProfileDetails(ProfileEntity profile) implements UserDetails {
    @Override
    public String getUsername() {
        return profile.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return profile.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + profile.getProfileRole().name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
}
