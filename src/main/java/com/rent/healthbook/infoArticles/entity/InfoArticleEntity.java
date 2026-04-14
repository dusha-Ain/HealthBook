package com.rent.healthbook.infoArticles.entity;

import java.sql.Date;
import java.util.UUID;

public class InfoArticleEntity {

    private UUID id;

    private String title;

    private String content;

    private Date created;

    private Boolean isPublished;

    private UUID idProfile;

    public InfoArticleEntity() {
    }

    public InfoArticleEntity(UUID id,
                             String title,
                             String content,
                             Date created,
                             Boolean isPublished,
                             UUID idProfile) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = created;
        this.isPublished = isPublished;
        this.idProfile = idProfile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public UUID getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(UUID idProfile) {
        this.idProfile = idProfile;
    }
}
