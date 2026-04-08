package com.rent.healthbook.profile.entity;


import com.rent.healthbook.profile.dto.enums.ProfileRole;

import java.sql.Date;
import java.util.UUID;

public class ProfileEntity {

    private UUID id;

    private String lastName;

    private String firstName;

    private String patronymic;

    private String email;

    private String password;

    private String phone;

    private Boolean isFired;

    private Date registrationDate;

    private ProfileRole profileRole;

    public ProfileEntity() {
    }

    public ProfileEntity(UUID id,
                         String lastName,
                         String firstName,
                         String patronymic,
                         String email,
                         String password,
                         String phone,
                         Boolean isFired,
                         Date registrationDate,
                         ProfileRole profileRole) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isFired = isFired;
        this.registrationDate = registrationDate;
        this.profileRole = profileRole;
    }

    public Boolean getFired() { return isFired;}

    public void setFired(Boolean fired) { isFired = fired; }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ProfileRole getProfileRole() {
        return profileRole;
    }

    public void setProfileRole(ProfileRole profileRole) {
        this.profileRole = profileRole;
    }
}
