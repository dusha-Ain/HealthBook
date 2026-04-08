--liquibase formatted sql

--changeset create:1
CREATE TABLE IF NOT EXISTS consultant_profile
(
    id UUID PRIMARY KEY,
    is_online BOOLEAN NOT NULL,
    id_profile UUID,
    CONSTRAINT FK_id_profile
        FOREIGN KEY (id_profile)
            REFERENCES profile (id)
            ON DELETE CASCADE
)