package com.talentomdd.repository;

import com.talentomdd.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudentId(Long studentId);
    List<Application> findByVacancyId(Long vacancyId);
    List<Application> findByVacancyCompanyId(Long companyId);
    Optional<Application> findByStudentIdAndVacancyId(Long studentId, Long vacancyId);
    boolean existsByStudentIdAndVacancyId(Long studentId, Long vacancyId);
}
