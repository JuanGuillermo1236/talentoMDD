package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Habilidad requerida por una vacante. El "required" permite en el futuro
 * distinguir habilidades obligatorias de deseables (usado por el motor
 * de matching de Antony si su contrato lo necesita).
 */
@Entity
@Table(name = "vacancy_requirements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacancyRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Builder.Default
    private boolean required = true;
}
