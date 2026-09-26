package com.talentomdd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCompanyProfileRequest {

    @NotBlank
    private String name;

    private String businessInfo;
    private String profileDescription;
    private String location;
}
