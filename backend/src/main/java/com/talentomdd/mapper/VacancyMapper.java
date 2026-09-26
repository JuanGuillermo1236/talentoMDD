package com.talentomdd.mapper;

import com.talentomdd.dto.VacancyResponse;
import com.talentomdd.entity.Vacancy;

public class VacancyMapper {

    private VacancyMapper() {}

    public static VacancyResponse toResponse(Vacancy vacancy) {
        return VacancyResponse.builder()
                .id(vacancy.getId())
                .companyId(vacancy.getCompany().getId())
                .companyName(vacancy.getCompany().getName())
                .title(vacancy.getTitle())
                .description(vacancy.getDescription())
                .modality(vacancy.getModality())
                .workingHours(vacancy.getWorkingHours())
                .status(vacancy.getStatus())
                .createdAt(vacancy.getCreatedAt())
                .requiredSkills(vacancy.getRequirements().stream()
                        .map(r -> r.getSkill().getName())
                        .toList())
                .build();
    }
}
