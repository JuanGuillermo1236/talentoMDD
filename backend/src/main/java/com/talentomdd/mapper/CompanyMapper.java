package com.talentomdd.mapper;

import com.talentomdd.dto.CompanyProfileResponse;
import com.talentomdd.entity.Company;

public class CompanyMapper {

    private CompanyMapper() {}

    public static CompanyProfileResponse toResponse(Company company) {
        return CompanyProfileResponse.builder()
                .id(company.getId())
                .email(company.getUser().getEmail())
                .name(company.getName())
                .businessInfo(company.getBusinessInfo())
                .profileDescription(company.getProfileDescription())
                .location(company.getLocation())
                .build();
    }
}
