package com.talentomdd.controller;

import com.talentomdd.dto.CompanyProfileResponse;
import com.talentomdd.dto.UpdateCompanyProfileRequest;
import com.talentomdd.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ARCHIVO: controller/CompanyController.java
 * DESCRIPCIÓN: perfil de la empresa autenticada.
 * ROL REQUERIDO: COMPANY.
 */
@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Perfil de empresa")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Obtener mi perfil de empresa")
    public ResponseEntity<CompanyProfileResponse> getMe(Authentication authentication) {
        return ResponseEntity.ok(companyService.getMyProfile(authentication.getName()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Actualizar mi perfil de empresa")
    public ResponseEntity<CompanyProfileResponse> updateMe(Authentication authentication,
                                                            @Valid @RequestBody UpdateCompanyProfileRequest request) {
        return ResponseEntity.ok(companyService.updateMyProfile(authentication.getName(), request));
    }
}
