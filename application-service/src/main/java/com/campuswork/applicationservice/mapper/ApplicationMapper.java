package com.campuswork.applicationservice.mapper;

import com.campuswork.applicationservice.dto.ApplicationResponse;
import com.campuswork.applicationservice.model.ApplicationStatus;
import com.campuswork.applicationservice.model.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public JobApplication toEntity(Long jobId, Long studentId) {
        return JobApplication.builder()
                .jobId(jobId)
                .studentId(studentId)
                .status(ApplicationStatus.PENDING)
                .build();
    }

    public ApplicationResponse toResponse(JobApplication app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .jobId(app.getJobId())
                .studentId(app.getStudentId())
                .status(app.getStatus())
                .createdAt(app.getCreatedAt())
                .build();
    }
}
