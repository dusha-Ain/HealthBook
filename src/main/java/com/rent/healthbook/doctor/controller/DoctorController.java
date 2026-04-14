package com.rent.healthbook.doctor.controller;

import com.rent.healthbook.doctor.dto.Doctor;
import com.rent.healthbook.doctor.dto.DoctorUpdate;
import com.rent.healthbook.doctor.dto.Doctors;
import com.rent.healthbook.doctor.service.DoctorService;
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
@RequestMapping("/api/doctors/")
@Tag(name = "Доктора", description = "Управление профилями-докторов")
public class DoctorController {

    Logger logger = LoggerFactory.getLogger(DoctorController.class);
    private final DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @Operation(summary = "Создать нового доктора")
    @PostMapping
    public ResponseEntity<Doctor> addConsultant(
            @Parameter(description = "Данные доктора")
            @Valid @RequestBody
            Doctor doctor) {
        logger.info("Called addDoctor");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addDoctor(doctor));
    }


    @Operation(summary = "Найти доктора по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @Parameter(description = "Идентификатор доктора")
            @PathVariable UUID id) {
        logger.info("Called getDoctorById = {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getDoctorById(id));
    }


    @Operation(summary = "Вывести всех докторов")
    @GetMapping("/")
    public ResponseEntity<Doctors> getAllDoctorsByFilter(
            @Parameter(description = "Количество докторов для вывода")
            @RequestParam(name = "pageSize", required = false) Integer pageSize,

            @Parameter(description = "Номер страницы поиска")
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        logger.info("Called getAllDoctors");
        var filter = new Pageable();
        filter.setPageNumber(pageNumber);
        filter.setPageSize(pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllDoctorsByFilter(filter));
    }


    @Operation(summary = "Редактировать данные доктора")
    @PutMapping("/update/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @Parameter(description = "Идентифкатор доктора")
            @PathVariable UUID id,
            @Parameter(description = "Данные для обновления доктора")
            @Valid @RequestBody DoctorUpdate profile) {
        logger.info("Called updateDoctor by id = {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.updateDoctor(id, profile));
    }


    @Operation(summary = "Удалить доктора")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @Parameter(description = "Идентифкатор доктора")
            @PathVariable UUID id) {
        logger.info("Called deleteDoctor by id = {}", id);
        service.deleteDoctor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
