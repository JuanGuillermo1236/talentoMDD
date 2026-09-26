package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Microcertificaciones del estudiante. PROVISIONAL: sin validar aun con Mijail.
 */
@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String title;

    private String issuer;

    private LocalDate issuedDate;

    private String credentialUrl;
}
