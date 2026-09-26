package com.talentomdd.dto;

import com.talentomdd.entity.SkillLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSkillRequest {

    @NotBlank
    private String skillName;

    @NotNull
    private SkillLevel level;
}
