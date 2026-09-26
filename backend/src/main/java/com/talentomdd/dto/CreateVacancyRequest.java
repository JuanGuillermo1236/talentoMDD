package com.talentomdd.dto;

import com.talentomdd.entity.Modality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateVacancyRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Modality modality;

    private String workingHours;

    @NotEmpty(message = "Debe indicar al menos una habilidad requerida")
    private List<String> requiredSkills;
}
