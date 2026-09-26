package com.talentomdd.dto;

import com.talentomdd.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull
    private Role role; // STUDENT o COMPANY (ADMIN no se autoregistra)

    @NotBlank
    private String fullName; // nombre del estudiante o nombre de la empresa
}
