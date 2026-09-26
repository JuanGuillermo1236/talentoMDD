package com.talentomdd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentProfileRequest {

    @NotBlank
    private String fullName;

    private String career;

    @PositiveOrZero
    private Integer cycle;

    private String availability;
    private String portfolioUrl;
    private String githubUrl;
    private String projectsDescription;
}
