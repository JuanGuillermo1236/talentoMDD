package com.talentomdd.controller;

import com.talentomdd.dto.ApplicationResponse;
import com.talentomdd.dto.CreateApplicationRequest;
import com.talentomdd.service.ApplicationService;
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
 * ARCHIVO: controller/ApplicationController.java
 * DESCRIPCIÓN: postulaciones de estudiantes a vacantes.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Postulaciones")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/api/v1/applications")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Postular a una vacante")
    public ResponseEntity<ApplicationResponse> apply(Authentication authentication,
                                                       @Valid @RequestBody CreateApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.apply(authentication.getName(), request));
    }

    @GetMapping("/api/v1/students/me/applications")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Listar mis postulaciones (como estudiante)")
    public ResponseEntity<List<ApplicationResponse>> myApplications(Authentication authentication) {
        return ResponseEntity.ok(applicationService.myApplicationsAsStudent(authentication.getName()));
    }

    @GetMapping("/api/v1/companies/me/applications")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Listar postulaciones recibidas (como empresa)")
    public ResponseEntity<List<ApplicationResponse>> receivedApplications(Authentication authentication) {
        return ResponseEntity.ok(applicationService.myApplicationsAsCompany(authentication.getName()));
    }
}
