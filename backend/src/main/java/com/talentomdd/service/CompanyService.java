package com.talentomdd.service;

import com.talentomdd.dto.CompanyProfileResponse;
import com.talentomdd.dto.UpdateCompanyProfileRequest;
import com.talentomdd.entity.Company;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.mapper.CompanyMapper;
import com.talentomdd.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyProfileResponse getMyProfile(String email) {
        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));
        return CompanyMapper.toResponse(company);
    }

    @Transactional
    public CompanyProfileResponse updateMyProfile(String email, UpdateCompanyProfileRequest request) {
        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));

        company.setName(request.getName());
        company.setBusinessInfo(request.getBusinessInfo());
        company.setProfileDescription(request.getProfileDescription());
        company.setLocation(request.getLocation());

        return CompanyMapper.toResponse(companyRepository.save(company));
    }
}
