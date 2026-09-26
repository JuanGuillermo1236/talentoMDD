package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Perfil de estudiante/egresado. PROVISIONAL: pendiente de validar contra
 * el esquema definitivo de Mijail (campos de portafolio/GitHub podrian
 * moverse a una tabla propia si crecen).
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String career; // carrera

    private Integer cycle; // ciclo

    private String availability; // disponibilidad

    private String portfolioUrl;

    private String githubUrl;

    @Column(columnDefinition = "TEXT")
    private String projectsDescription;

    @Builder.Default
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentSkill> skills = new ArrayList<>();
}
