package com.talentomdd.controller;

import com.talentomdd.dto.CreateVacancyRequest;
import com.talentomdd.dto.UpdateVacancyRequest;
import com.talentomdd.dto.VacancyResponse;
import com.talentomdd.service.VacancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ARCHIVO: controller/VacancyController.java
 * DESCRIPCIÓN: CRUD de vacantes. Lectura pública, escritura solo COMPANY (dueña de la vacante).
 */
@RestController
@RequestMapping("/api/v1/vacancies")
@RequiredArgsConstructor
@Tag(name = "Vacancies", description = "Gestión de vacantes")
public class VacancyController {

    private final VacancyService vacancyService;

    @GetMapping
    @Operation(summary = "Listar vacantes activas (público)")
    public ResponseEntity<List<VacancyResponse>> list() {
        return ResponseEntity.ok(vacancyService.listActiveVacancies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener el detalle de una vacante (público)")
    public ResponseEntity<VacancyResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Crear una vacante")
    public ResponseEntity<VacancyResponse> create(Authentication authentication,
                                                   @Valid @RequestBody CreateVacancyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vacancyService.create(authentication.getName(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Editar una vacante propia")
    public ResponseEntity<VacancyResponse> update(Authentication authentication,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody UpdateVacancyRequest request) {
        return ResponseEntity.ok(vacancyService.update(authentication.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Eliminar una vacante propia")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Long id) {
        vacancyService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
