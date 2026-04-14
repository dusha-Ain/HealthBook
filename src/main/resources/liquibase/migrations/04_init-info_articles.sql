--liquibase formatted sql

--changeset create:1
CREATE TABLE IF NOT EXISTS info_articles
(
    id           UUID PRIMARY KEY,
    title        VARCHAR(50) NOT NULL,
    content      TEXT,
    created      DATE,
    is_published BOOLEAN     NOT NULL,
    id_profile   UUID        NOT NULL,
    CONSTRAINT FK_id_profile
        FOREIGN KEY (id_profile)
            REFERENCES profile (id)
            ON DELETE CASCADE
)