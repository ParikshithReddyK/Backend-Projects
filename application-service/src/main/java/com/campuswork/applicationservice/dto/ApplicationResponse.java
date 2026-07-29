package com.campuswork.applicationservice.dto;

import com.campuswork.applicationservice.model.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private Long studentId;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
}