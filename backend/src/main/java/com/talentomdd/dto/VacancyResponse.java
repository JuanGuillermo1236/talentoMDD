package com.talentomdd.dto;

import com.talentomdd.entity.Modality;
import com.talentomdd.entity.VacancyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class VacancyResponse {
    private Long id;
    private Long companyId;
    private String companyName;
    private String title;
    private String description;
    private Modality modality;
    private String workingHours;
    private VacancyStatus status;
    private LocalDateTime createdAt;
    private List<String> requiredSkills;
}
