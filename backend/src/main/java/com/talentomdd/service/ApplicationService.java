package com.talentomdd.service;

import com.talentomdd.dto.ApplicationResponse;
import com.talentomdd.dto.CreateApplicationRequest;
import com.talentomdd.entity.Application;
import com.talentomdd.entity.ApplicationStatus;
import com.talentomdd.entity.Student;
import com.talentomdd.entity.Vacancy;
import com.talentomdd.entity.VacancyStatus;
import com.talentomdd.exception.DuplicateResourceException;
import com.talentomdd.exception.ForbiddenOperationException;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.mapper.ApplicationMapper;
import com.talentomdd.repository.ApplicationRepository;
import com.talentomdd.repository.CompanyRepository;
import com.talentomdd.repository.StudentRepository;
import com.talentomdd.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;

    /**
     * Registra una postulación. Reglas (ver prompt del proyecto):
     * 1. Usuario autenticado -> garantizado por Spring Security antes de llegar aquí.
     * 2. Debe ser STUDENT -> garantizado por @PreAuthorize en el controller.
     * 3. La vacante debe existir.
     * 4. La vacante debe estar activa.
     * 5. No debe existir una postulación duplicada.
     * 6. Registrar la postulación.
     */
    @Transactional
    public ApplicationResponse apply(String studentEmail, CreateApplicationRequest request) {
        Student student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));

        Vacancy vacancy = vacancyRepository.findById(request.getVacancyId())
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + request.getVacancyId()));

        if (vacancy.getStatus() != VacancyStatus.ACTIVE) {
            throw new IllegalArgumentException("La vacante no está activa");
        }

        if (applicationRepository.existsByStudentIdAndVacancyId(student.getId(), vacancy.getId())) {
            throw new DuplicateResourceException("Ya existe una postulación a esta vacante");
        }

        Application application = Application.builder()
                .student(student)
                .vacancy(vacancy)
                .status(ApplicationStatus.PENDING)
                .build();

        return ApplicationMapper.toResponse(applicationRepository.save(application));
    }

    public List<ApplicationResponse> myApplicationsAsStudent(String studentEmail) {
        Student student = studentRepository.findByUserEmail(studentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));

        return applicationRepository.findByStudentId(student.getId()).stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    public List<ApplicationResponse> myApplicationsAsCompany(String companyEmail) {
        var company = companyRepository.findByUserEmail(companyEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));

        return applicationRepository.findByVacancyCompanyId(company.getId()).stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }
}
