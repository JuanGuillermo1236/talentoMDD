package com.talentomdd.matching;

import com.talentomdd.dto.CandidateMatchResponse;
import com.talentomdd.dto.MatchResponse;
import com.talentomdd.entity.Application;
import com.talentomdd.entity.Student;
import com.talentomdd.entity.StudentSkill;
import com.talentomdd.entity.Vacancy;
import com.talentomdd.entity.VacancyRequirement;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.repository.ApplicationRepository;
import com.talentomdd.repository.StudentRepository;
import com.talentomdd.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * IMPLEMENTACIÓN PROVISIONAL / TEMPORAL.
 *
 * Esta clase existe solo para que el resto del backend (endpoints de
 * matching, candidatos) sea funcional mientras Antony entrega el motor
 * definitivo. Calcula un porcentaje simple de coincidencia de habilidades
 * (intersección de nombres de habilidad, sin ponderar nivel).
 *
 * CUANDO ANTONY ENTREGUE SU IMPLEMENTACIÓN:
 * 1. Crear una nueva clase que implemente MatchingService (o adaptar esta).
 * 2. Marcarla como el @Primary/@Service activo.
 * 3. Eliminar o desactivar esta clase stub.
 * 4. Validar que el contrato de salida (MatchResponse/CandidateMatchResponse)
 *    siga siendo compatible; si Antony necesita cambiarlo, coordinar con
 *    el equipo antes de romper el contrato.
 */
@Service
@RequiredArgsConstructor
public class MatchingServiceStubImpl implements MatchingService {

    private final StudentRepository studentRepository;
    private final VacancyRepository vacancyRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public MatchResponse calculateMatch(Long studentId, Long vacancyId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado: " + studentId));
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + vacancyId));

        return buildMatch(student, vacancy);
    }

    @Override
    public List<CandidateMatchResponse> findCandidatesByMatch(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + vacancyId));

        // PROVISIONAL: recorre estudiantes que ya postularon a la vacante.
        // Antony podría requerir recorrer todo el universo de estudiantes;
        // eso se define junto con el contrato definitivo del motor.
        List<Application> applications = applicationRepository.findByVacancyId(vacancyId);

        return applications.stream()
                .map(app -> {
                    MatchResponse match = buildMatch(app.getStudent(), vacancy);
                    return CandidateMatchResponse.builder()
                            .studentId(app.getStudent().getId())
                            .studentName(app.getStudent().getFullName())
                            .score(match.getScore())
                            .matchedSkills(match.getMatchedSkills())
                            .missingSkills(match.getMissingSkills())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private MatchResponse buildMatch(Student student, Vacancy vacancy) {
        Set<String> studentSkillNames = student.getSkills().stream()
                .map(StudentSkill::getSkill)
                .map(s -> s.getName().toLowerCase())
                .collect(Collectors.toSet());

        List<String> requiredSkillNames = vacancy.getRequirements().stream()
                .map(VacancyRequirement::getSkill)
                .map(s -> s.getName())
                .toList();

        List<String> matched = requiredSkillNames.stream()
                .filter(name -> studentSkillNames.contains(name.toLowerCase()))
                .toList();

        List<String> missing = requiredSkillNames.stream()
                .filter(name -> !studentSkillNames.contains(name.toLowerCase()))
                .toList();

        int score = requiredSkillNames.isEmpty() ? 0 :
                (int) Math.round((matched.size() * 100.0) / requiredSkillNames.size());

        return MatchResponse.builder()
                .studentId(student.getId())
                .vacancyId(vacancy.getId())
                .score(score)
                .matchedSkills(matched)
                .missingSkills(missing)
                .build();
    }
}
