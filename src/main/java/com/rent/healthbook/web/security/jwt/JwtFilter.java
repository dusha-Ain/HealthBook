package com.rent.healthbook.web.security.jwt;

import com.rent.healthbook.web.security.CustomProfileService;
import com.rent.healthbook.web.security.dto.CustomProfileDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomProfileService customProfileService;

    public JwtFilter(JwtService jwtService,
                     CustomProfileService customProfileService) {
        this.jwtService = jwtService;
        this.customProfileService = customProfileService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtService.validateToken(token)){
            setCustomProfileDetailsToSecurityContextHilder(token);
        }
        filterChain.doFilter(request, response);
    }

    private void setCustomProfileDetailsToSecurityContextHilder(String token) {
        String email = jwtService.getEmailFromToken(token);
        CustomProfileDetails customProfileDetails = customProfileService.loadUserByUsername(email);
        var authentication = new UsernamePasswordAuthenticationToken(
                customProfileDetails, null, customProfileDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
