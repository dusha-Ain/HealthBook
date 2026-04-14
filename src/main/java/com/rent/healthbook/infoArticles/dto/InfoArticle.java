package com.rent.healthbook.infoArticles.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.sql.Date;
import java.util.UUID;

public record InfoArticle(
        @Null
        UUID id,

        @NotBlank(message = "Заголок не должен быть пустым")
        @Size(max = 50, message = "Слишком длинное название")
        @Schema(description = "Заголовок", example = "Как записаться на приём?")
        String title,

        @Schema(description = "Текст статьи", example = "Для начала регистриуетсь в системе...")
        String content,

        @Null
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Schema(example = "2026-03-20")
        Date  created,

        @Null
        @Schema(description = "Опубликована ли статья", example = "FALSE")
        Boolean isPublished,

        @NotNull
        @Schema(description = "Идентификатор профиля, который написал статью")
        UUID idProfile
) {
}
