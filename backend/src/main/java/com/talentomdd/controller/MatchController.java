package com.talentomdd.controller;

import com.talentomdd.dto.CandidateMatchResponse;
import com.talentomdd.dto.MatchResponse;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.matching.MatchingService;
import com.talentomdd.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ARCHIVO: controller/MatchController.java
 * DESCRIPCIÓN: expone el resultado del MatchingService (interfaz de Antony).
 * PROVISIONAL: usa MatchingServiceStubImpl hasta integrar el motor definitivo.
 */
@RestController
@RequestMapping("/api/v1/vacancies")
@RequiredArgsConstructor
@Tag(name = "Matching", description = "Compatibilidad estudiante-vacante")
public class MatchController {

    private final MatchingService matchingService;
    private final StudentRepository studentRepository;

    @GetMapping("/{vacancyId}/match")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Calcular mi compatibilidad con una vacante")
    public ResponseEntity<MatchResponse> match(Authentication authentication, @PathVariable Long vacancyId) {
        Long studentId = studentRepository.findByUserEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"))
                .getId();
        return ResponseEntity.ok(matchingService.calculateMatch(studentId, vacancyId));
    }

    @GetMapping("/{vacancyId}/candidates")
    @PreAuthorize("hasRole('COMPANY')")
    @Operation(summary = "Listar candidatos ordenables por compatibilidad con la vacante")
    public ResponseEntity<List<CandidateMatchResponse>> candidates(@PathVariable Long vacancyId) {
        return ResponseEntity.ok(matchingService.findCandidatesByMatch(vacancyId));
    }
}
