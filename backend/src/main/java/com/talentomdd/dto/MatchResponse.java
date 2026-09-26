package com.talentomdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Contrato de salida del motor de matching de Antony.
 * NO modificar la forma de este contrato sin coordinar con Antony.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MatchResponse {
    private Long studentId;
    private Long vacancyId;
    private int score; // 0-100
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
