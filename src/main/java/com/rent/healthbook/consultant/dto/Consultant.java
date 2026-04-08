package com.rent.healthbook.consultant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record Consultant(
        @Null
        UUID id,

        @Null
        Boolean isOnline,

        @NotNull
        @Schema(description = "Идентификатор профиля")
        UUID idProfile
) {
}
