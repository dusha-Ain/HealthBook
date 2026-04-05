package com.rent.healthbook.doctor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public record Doctor(
    @Null
    UUID id,

    @NotBlank(message = "Специализация не должна быть пустой")
    @Size(max = 20)
    @Schema(description = "Специализация доктора", example = "Нейрохирург")
    String specialization,

    @Size(max = 20)
    @Schema(description = "Номер кабинета доктора", example = "121")
    String cabinetNumber,

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата начала работы", example = "2026-03-20")
    Date workStart,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата конца раюлты", example = "2029-03-20")
    Date workEnd,

    @JsonFormat(pattern = "HH:mm:ss")
    @Schema(description = "Длительность приёма", example = "00:30:00")
    Time durationReception,

    @NotNull
    @Schema(description = "Идентификатор профиля")
    UUID idProfile
) {
}
