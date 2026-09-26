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
public class CandidateMatchResponse {
    private Long studentId;
    private String studentName;
    private int score;
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
