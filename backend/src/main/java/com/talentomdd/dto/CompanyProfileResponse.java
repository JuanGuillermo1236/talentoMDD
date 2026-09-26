package com.talentomdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CompanyProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String businessInfo;
    private String profileDescription;
    private String location;
}
