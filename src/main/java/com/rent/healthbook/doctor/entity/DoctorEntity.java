package com.rent.healthbook.doctor.entity;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class DoctorEntity {

    private UUID id;

    private String specialization;

    private String cabinetNumber;

    private Date workStart;

    private Date workEnd;

    private Time durationReception;

    private UUID idProfile;

    public DoctorEntity() {
    }

    public DoctorEntity(UUID id,
                        String specialization,
                        String cabinetNumber,
                        Date workStart,
                        Date workEnd,
                        Time durationReception,
                        UUID idProfile) {
        this.id = id;
        this.specialization = specialization;
        this.cabinetNumber = cabinetNumber;
        this.workStart = workStart;
        this.workEnd = workEnd;
        this.durationReception = durationReception;
        this.idProfile = idProfile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCabinetNumber() {
        return cabinetNumber;
    }

    public void setCabinetNumber(String cabinetNumber) {
        this.cabinetNumber = cabinetNumber;
    }

    public Date getWorkStart() {
        return workStart;
    }

    public void setWorkStart(Date workStart) {
        this.workStart = workStart;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }

    public Time getDurationReception() {
        return durationReception;
    }

    public void setDurationReception(Time durationReception) {
        this.durationReception = durationReception;
    }

    public UUID getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(UUID idProfile) {
        this.idProfile = idProfile;
    }
}
