package com.rent.healthbook.web.security;

import com.rent.healthbook.profile.service.ProfileService;
import com.rent.healthbook.web.security.dto.JwtAuthentication;
import com.rent.healthbook.web.security.dto.ProfileCredential;
import com.rent.healthbook.web.security.dto.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ProfileService profileService;
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthentication> signIn(
            @RequestBody ProfileCredential profileCredential){
        logger.info("Called signIn = {}", profileCredential.getEmail());
        JwtAuthentication jwtAuthentication = profileService.signIn(profileCredential);
        return ResponseEntity.ok(jwtAuthentication);
    }

    @PostMapping("/refresh")
    public JwtAuthentication refresh(@RequestBody RefreshToken refreshToken){
        return profileService.refreshToken(refreshToken);
    }
}
