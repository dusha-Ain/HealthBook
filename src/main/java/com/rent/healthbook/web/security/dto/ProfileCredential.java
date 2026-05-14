package com.rent.healthbook.web.security.dto;


public class ProfileCredential {
    private String email;
    private String password;

    public ProfileCredential() {
    }

    public ProfileCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
