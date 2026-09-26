package com.talentomdd.dto;

import com.talentomdd.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long vacancyId;
    private String vacancyTitle;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
}
