package com.talentomdd.controller;

import com.talentomdd.dto.AddSkillRequest;
import com.talentomdd.dto.StudentProfileResponse;
import com.talentomdd.dto.UpdateStudentProfileRequest;
import com.talentomdd.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ARCHIVO: controller/StudentController.java
 * DESCRIPCIÓN: perfil y habilidades del estudiante autenticado.
 * ROL REQUERIDO: STUDENT.
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Perfil y habilidades del estudiante")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Obtener mi perfil de estudiante")
    public ResponseEntity<StudentProfileResponse> getMe(Authentication authentication) {
        return ResponseEntity.ok(studentService.getMyProfile(authentication.getName()));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Actualizar mi perfil de estudiante")
    public ResponseEntity<StudentProfileResponse> updateMe(Authentication authentication,
                                                            @Valid @RequestBody UpdateStudentProfileRequest request) {
        return ResponseEntity.ok(studentService.updateMyProfile(authentication.getName(), request));
    }

    @GetMapping("/me/skills")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Listar mis habilidades")
    public ResponseEntity<StudentProfileResponse> getMySkills(Authentication authentication) {
        return ResponseEntity.ok(studentService.getMyProfile(authentication.getName()));
    }

    @PostMapping("/me/skills")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Agregar una habilidad a mi perfil")
    public ResponseEntity<StudentProfileResponse> addSkill(Authentication authentication,
                                                            @Valid @RequestBody AddSkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.addSkill(authentication.getName(), request));
    }

    @DeleteMapping("/me/skills/{skillId}")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Eliminar una habilidad de mi perfil")
    public ResponseEntity<Void> deleteSkill(Authentication authentication, @PathVariable Long skillId) {
        studentService.removeSkill(authentication.getName(), skillId);
        return ResponseEntity.noContent().build();
    }
}
