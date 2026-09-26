package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Perfil de empresa. PROVISIONAL: campos de ubicacion pueden normalizarse
 * en tablas de departamento/provincia si Mijail lo define asi.
 */
@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String businessInfo;

    @Column(columnDefinition = "TEXT")
    private String profileDescription;

    private String location; // ciudad/region en Peru
}
