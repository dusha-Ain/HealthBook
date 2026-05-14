package com.rent.healthbook.web.security.dto;

public class JwtAuthentication {
    private String token;
    private String refreshToken;


    public JwtAuthentication() {
    }

    public JwtAuthentication(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
