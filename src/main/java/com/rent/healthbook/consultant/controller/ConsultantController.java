package com.rent.healthbook.consultant.controller;

import com.rent.healthbook.consultant.dto.Consultant;
import com.rent.healthbook.consultant.dto.Consultants;
import com.rent.healthbook.consultant.service.ConsultantService;
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
@RequestMapping("/api/profiles-consultant/")
@Tag(name = "Консультанты", description = "Управление профилями-консультантами")
public class ConsultantController {

    Logger logger = LoggerFactory.getLogger(ConsultantController.class);
    private final ConsultantService service;

    public ConsultantController(ConsultantService service) {
        this.service = service;
    }

    @Operation(summary = "Создать нового консультанта")
    @PostMapping
    public ResponseEntity<Consultant> addConsultant(
            @Parameter(description = "Данные консультанта")
            @Valid @RequestBody
            Consultant consultant) {
        logger.info("Called addConsultant");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addConsultant(consultant));
    }


    @Operation(summary = "Найти консультанта по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Consultant> getConsultantById(
            @Parameter(description = "Идентификатор консультанта")
            @PathVariable UUID id) {
        logger.info("Called getConsultantById = {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getById(id));

    }


    @Operation(summary = "Вывести всех консультантов")
    @GetMapping("/")
    public ResponseEntity<Consultants> getAllConsultants() {
        logger.info("Called getAllConsultants");
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllConsultants());
    }


    @Operation(summary = "Изменить статус онлайна")
    @PutMapping("update/web/{id}")
    public ResponseEntity<Void> updateConsultantOnline(
            @Parameter(description = "Идентифкатор консультанта")
            @PathVariable UUID id,
            @Parameter(description = "Статус онлайна")
            @RequestBody Boolean online){
        logger.info("Called updateConsultantOnline by id = {}", id);
        service.updateConsultantOnline(id, online);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Operation(summary = "Удалить консультанта")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteConsultant(
            @Parameter(description = "Идентифкатор консультанта")
            @PathVariable UUID id) {
        logger.info("Called deleteConsultant by id = {}", id);
        service.deleteConsultant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
