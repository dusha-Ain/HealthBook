package com.rent.healthbook.consultant.entity;

import java.util.UUID;

public class ConsultantEntity {
    private UUID id;
    private Boolean isOnline;
    private UUID idProfile;

    public ConsultantEntity() {
    }

    public ConsultantEntity(UUID id, Boolean isOnline, UUID idProfile) {
        this.id = id;
        this.isOnline = isOnline;
        this.idProfile = idProfile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public UUID getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(UUID idProfile) {
        this.idProfile = idProfile;
    }
}
