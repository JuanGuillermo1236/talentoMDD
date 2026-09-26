package com.talentomdd.mapper;

import com.talentomdd.dto.SkillResponse;
import com.talentomdd.dto.StudentProfileResponse;
import com.talentomdd.entity.Student;
import com.talentomdd.entity.StudentSkill;

import java.util.List;

public class StudentMapper {

    private StudentMapper() {}

    public static StudentProfileResponse toResponse(Student student) {
        List<SkillResponse> skills = student.getSkills() == null ? List.of() :
                student.getSkills().stream().map(StudentMapper::toSkillResponse).toList();

        return StudentProfileResponse.builder()
                .id(student.getId())
                .email(student.getUser().getEmail())
                .fullName(student.getFullName())
                .career(student.getCareer())
                .cycle(student.getCycle())
                .availability(student.getAvailability())
                .portfolioUrl(student.getPortfolioUrl())
                .githubUrl(student.getGithubUrl())
                .projectsDescription(student.getProjectsDescription())
                .skills(skills)
                .build();
    }

    public static SkillResponse toSkillResponse(StudentSkill studentSkill) {
        return SkillResponse.builder()
                .id(studentSkill.getSkill().getId())
                .name(studentSkill.getSkill().getName())
                .level(studentSkill.getLevel())
                .build();
    }
}
