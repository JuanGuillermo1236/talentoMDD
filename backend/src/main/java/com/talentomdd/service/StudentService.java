package com.talentomdd.service;

import com.talentomdd.dto.AddSkillRequest;
import com.talentomdd.dto.StudentProfileResponse;
import com.talentomdd.dto.UpdateStudentProfileRequest;
import com.talentomdd.entity.Skill;
import com.talentomdd.entity.Student;
import com.talentomdd.entity.StudentSkill;
import com.talentomdd.exception.DuplicateResourceException;
import com.talentomdd.exception.ResourceNotFoundException;
import com.talentomdd.mapper.StudentMapper;
import com.talentomdd.repository.SkillRepository;
import com.talentomdd.repository.StudentRepository;
import com.talentomdd.repository.StudentSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;

    public StudentProfileResponse getMyProfile(String email) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));
        return StudentMapper.toResponse(student);
    }

    @Transactional
    public StudentProfileResponse updateMyProfile(String email, UpdateStudentProfileRequest request) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));

        student.setFullName(request.getFullName());
        student.setCareer(request.getCareer());
        student.setCycle(request.getCycle());
        student.setAvailability(request.getAvailability());
        student.setPortfolioUrl(request.getPortfolioUrl());
        student.setGithubUrl(request.getGithubUrl());
        student.setProjectsDescription(request.getProjectsDescription());

        return StudentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentProfileResponse addSkill(String email, AddSkillRequest request) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));

        Skill skill = skillRepository.findByNameIgnoreCase(request.getSkillName())
                .orElseGet(() -> skillRepository.save(Skill.builder().name(request.getSkillName()).build()));

        studentSkillRepository.findByStudentIdAndSkillId(student.getId(), skill.getId())
                .ifPresent(s -> {
                    throw new DuplicateResourceException("La habilidad ya está registrada en el perfil");
                });

        StudentSkill studentSkill = StudentSkill.builder()
                .student(student)
                .skill(skill)
                .level(request.getLevel())
                .build();
        studentSkillRepository.save(studentSkill);

        Student refreshed = studentRepository.findById(student.getId()).orElseThrow();
        return StudentMapper.toResponse(refreshed);
    }

    @Transactional
    public void removeSkill(String email, Long skillId) {
        Student student = studentRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de estudiante no encontrado"));

        studentSkillRepository.findByStudentIdAndSkillId(student.getId(), skillId)
                .orElseThrow(() -> new ResourceNotFoundException("La habilidad no está registrada en el perfil"));

        studentSkillRepository.deleteByStudentIdAndSkillId(student.getId(), skillId);
    }
}
