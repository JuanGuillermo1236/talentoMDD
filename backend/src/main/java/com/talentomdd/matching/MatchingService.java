package com.talentomdd.matching;

import com.talentomdd.dto.CandidateMatchResponse;
import com.talentomdd.dto.MatchResponse;

import java.util.List;

/**
 * Contrato de integración con el motor de matching.
 *
 * IMPORTANTE: Antony es responsable de la fórmula definitiva. El backend
 * NO debe implementar una segunda versión del algoritmo; esta interfaz
 * define únicamente el contrato de entrada/salida que el resto del
 * backend utilizará, sin conocer los detalles internos de la fórmula.
 *
 * Contrato de entrada esperado (a validar con Antony):
 *   - studentId: id del estudiante
 *   - vacancyId: id de la vacante
 *
 * Contrato de salida esperado (a validar con Antony), ver MatchResponse:
 *   {
 *     "score": 85,
 *     "matchedSkills": ["SQL", "Git", "JavaScript"],
 *     "missingSkills": ["React"]
 *   }
 */
public interface MatchingService {

    MatchResponse calculateMatch(Long studentId, Long vacancyId);

    List<CandidateMatchResponse> findCandidatesByMatch(Long vacancyId);
}
