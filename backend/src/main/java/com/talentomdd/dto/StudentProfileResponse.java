package com.talentomdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StudentProfileResponse {
    private Long id;
    private String email;
    private String fullName;
    private String career;
    private Integer cycle;
    private String availability;
    private String portfolioUrl;
    private String githubUrl;
    private String projectsDescription;
    private List<SkillResponse> skills;
}
