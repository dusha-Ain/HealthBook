--liquibase formatted sql

--changeset create:1
CREATE TABLE IF NOT EXISTS doctor_profile
(
    id UUID PRIMARY KEY,
    specialization VARCHAR(20) NOT NULL,
    cabinet_number VARCHAR(20),
    work_start DATE NOT NULL,
    work_end DATE,
    duration_reception TIME,
    id_profile UUID,
    CONSTRAINT FK_id_profile
        FOREIGN KEY (id_profile)
            REFERENCES profile (id)
            ON DELETE CASCADE
)