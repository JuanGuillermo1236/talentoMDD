package com.talentomdd.repository;

import com.talentomdd.entity.Vacancy;
import com.talentomdd.entity.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findByStatus(VacancyStatus status);
    List<Vacancy> findByCompanyId(Long companyId);
}
