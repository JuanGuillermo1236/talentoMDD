package com.talentomdd.mapper;

import com.talentomdd.dto.ApplicationResponse;
import com.talentomdd.entity.Application;

public class ApplicationMapper {

    private ApplicationMapper() {}

    public static ApplicationResponse toResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .studentId(application.getStudent().getId())
                .studentName(application.getStudent().getFullName())
                .vacancyId(application.getVacancy().getId())
                .vacancyTitle(application.getVacancy().getTitle())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .build();
    }
}
