package com.rent.healthbook.infoArticles.controller;

import com.rent.healthbook.infoArticles.dto.InfoArticle;
import com.rent.healthbook.infoArticles.dto.InfoArticles;
import com.rent.healthbook.infoArticles.service.InfoArticlesService;
import com.rent.healthbook.web.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/info-articles/")
@Tag(name = "Статьи", description = "Управление статьями")
public class InfoArticlesController {

    Logger logger = LoggerFactory.getLogger(InfoArticlesController.class);
    private final InfoArticlesService service;

    public InfoArticlesController(InfoArticlesService service) {
        this.service = service;
    }

    @Operation(summary = "Создать новую статью")
    @PostMapping
    public ResponseEntity<InfoArticle> addArticle(
            @Parameter(description = "Информация о статье")
            @Valid @RequestBody
            InfoArticle article) {
        logger.info("Called addArticle");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addArticle(article));
    }


    @Operation(summary = "Найти статью по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<InfoArticle> getArticleById(
            @Parameter(description = "Идентификатор статьи")
            @PathVariable UUID id) {
        logger.info("Called getArticleById = {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getArticleById(id));
    }

    @Operation(summary = "Вывести все статьи")
    @GetMapping("/")
    public ResponseEntity<InfoArticles> getAllArticlesByFilter(
            @Parameter(description = "Количество статей для вывода")
            @RequestParam(name = "pageSize", required = false) Integer pageSize,

            @Parameter(description = "Номер страницы поиска")
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        logger.info("Called getAllArticleByFilter");
        var filter = new Pageable();
        filter.setPageNumber(pageNumber);
        filter.setPageSize(pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllDoctorsByFilter(filter));
    }


    @Operation(summary = "Редактировать информацию о статье")
    @PutMapping("/update/{id}")
    public ResponseEntity<InfoArticle> updateArticle(
            @Parameter(description = "Идентифкатор статьи")
            @PathVariable UUID id,
            @Parameter(description = "Данные для обновления статьи")
            @Valid @RequestBody InfoArticle article) {
        logger.info("Called updateArticle by id = {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateArticle(id, article));
    }

    @Operation(summary = "Обновить статус публикации")
    @PutMapping("/update/publish/{id}")
    public ResponseEntity<Void> updateStatusArticle(
            @Parameter(description = "Идентифкатор статьи")
            @PathVariable UUID id,
            @Parameter(description = "Данные для обновления статьи")
            @Valid @RequestBody Boolean status) {
        logger.info("Called updateStatusArticle by id = {}", id);
        service.updateStatusArticle(id, status);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "Удалить статью")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "Идентифкатор доктора")
            @PathVariable UUID id) {
        logger.info("Called deleteArticle by id = {}", id);
        service.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
