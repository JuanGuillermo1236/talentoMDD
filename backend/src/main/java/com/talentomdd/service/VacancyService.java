package com.talentomdd.service;

import com.talentomdd.dto.CreateVacancyRequest;
import com.talentomdd.dto.UpdateVacancyRequest;
import com.talentomdd.dto.VacancyResponse;
import com.talentomdd.entity.Company;
import com.talentomdd.entity.Skill;
import com.talentomdd.entity.Vacancy;
import com.talentomdd.entity.VacancyRequirement;
import com.talentomdd.entity.VacancyStatus;
import com.talentomdd.exception.ForbiddenOperationException;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.mapper.VacancyMapper;
import com.talentomdd.repository.CompanyRepository;
import com.talentomdd.repository.SkillRepository;
import com.talentomdd.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;

    public List<VacancyResponse> listActiveVacancies() {
        return vacancyRepository.findByStatus(VacancyStatus.ACTIVE).stream()
                .map(VacancyMapper::toResponse)
                .toList();
    }

    public VacancyResponse getById(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + id));
        return VacancyMapper.toResponse(vacancy);
    }

    @Transactional
    public VacancyResponse create(String companyEmail, CreateVacancyRequest request) {
        Company company = companyRepository.findByUserEmail(companyEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));

        Vacancy vacancy = Vacancy.builder()
                .company(company)
                .title(request.getTitle())
                .description(request.getDescription())
                .modality(request.getModality())
                .workingHours(request.getWorkingHours())
                .status(VacancyStatus.ACTIVE)
                .requirements(new ArrayList<>())
                .build();

        Vacancy saved = vacancyRepository.save(vacancy);
        attachRequirements(saved, request.getRequiredSkills());

        return VacancyMapper.toResponse(vacancyRepository.save(saved));
    }

    @Transactional
    public VacancyResponse update(String companyEmail, Long vacancyId, UpdateVacancyRequest request) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + vacancyId));

        assertOwnership(vacancy, companyEmail);

        vacancy.setTitle(request.getTitle());
        vacancy.setDescription(request.getDescription());
        vacancy.setModality(request.getModality());
        vacancy.setWorkingHours(request.getWorkingHours());
        vacancy.setStatus(request.getStatus());

        vacancy.getRequirements().clear();
        attachRequirements(vacancy, request.getRequiredSkills());

        return VacancyMapper.toResponse(vacancyRepository.save(vacancy));
    }

    @Transactional
    public void delete(String companyEmail, Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada: " + vacancyId));

        assertOwnership(vacancy, companyEmail);
        vacancyRepository.delete(vacancy);
    }

    private void attachRequirements(Vacancy vacancy, List<String> skillNames) {
        for (String name : skillNames) {
            Skill skill = skillRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> skillRepository.save(Skill.builder().name(name).build()));

            vacancy.getRequirements().add(VacancyRequirement.builder()
                    .vacancy(vacancy)
                    .skill(skill)
                    .required(true)
                    .build());
        }
    }

    private void assertOwnership(Vacancy vacancy, String companyEmail) {
        if (!vacancy.getCompany().getUser().getEmail().equalsIgnoreCase(companyEmail)) {
            throw new ForbiddenOperationException("No tiene permiso para modificar esta vacante");
        }
    }
}
