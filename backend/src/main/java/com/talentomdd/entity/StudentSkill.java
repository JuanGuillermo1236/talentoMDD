package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Relacion N:M entre Student y Skill, con nivel de habilidad.
 */
@Entity
@Table(name = "student_skills", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillLevel level;
}
