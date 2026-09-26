package com.talentomdd.dto;

import com.talentomdd.entity.Modality;
import com.talentomdd.entity.VacancyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateVacancyRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Modality modality;

    private String workingHours;

    @NotNull
    private VacancyStatus status;

    @NotEmpty
    private List<String> requiredSkills;
}
