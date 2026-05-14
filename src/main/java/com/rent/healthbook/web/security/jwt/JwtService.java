package com.rent.healthbook.web.security.jwt;

import com.rent.healthbook.web.security.dto.JwtAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    @Value("X9qV7fL2mP8kR3dJ6nS1bT5wC4yA0zE7gH9uI2oK5rM4nB8vC6xZ1lL3pO0iU9yT7")
    private String jwtSecret;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public JwtAuthentication generateAuthToken(String email, String role, UUID id) {
        var jwt = new JwtAuthentication();
        jwt.setToken(generateJwtToken(email, role, id));
        jwt.setRefreshToken(generateRefreshToken(email, role, id));
        return jwt;
    }

    public JwtAuthentication refreshBaseToken(String email, String role, UUID id, String refreshToken) {
        var jwt = new JwtAuthentication();
        jwt.setToken(generateJwtToken(email, role, id));
        jwt.setRefreshToken(refreshToken);
        return jwt;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public String getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("id", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("role", String.class);
    }


    public String generateJwtToken(String email, String role, UUID id) {
        Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .claim("id", id.toString())
                .claim("role", role)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String email, String role, UUID id) {
        Date date = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(email)
                .claim("id", id.toString())
                .claim("role", role)
                .expiration(date)
                .signWith(getSecretKey())
                .compact();
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
