package com.rent.healthbook.consultant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.UUID;

public record Consultant(
        @Null
        UUID id,

        @Null
        Boolean isOnline,

        @NotNull
        UUID idProfile
) {
}
