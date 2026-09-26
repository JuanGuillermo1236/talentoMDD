package com.talentomdd.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Catalogo maestro de habilidades (ej. SQL, Git, React).
 */
@Entity
@Table(name = "skills", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
