package com.talentomdd.repository;

import com.talentomdd.entity.VacancyRequirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRequirementRepository extends JpaRepository<VacancyRequirement, Long> {
    List<VacancyRequirement> findByVacancyId(Long vacancyId);
}
