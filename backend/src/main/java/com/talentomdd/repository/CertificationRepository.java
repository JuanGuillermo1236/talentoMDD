package com.talentomdd.repository;

import com.talentomdd.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByStudentId(Long studentId);
}
