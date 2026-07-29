package com.campuswork.certificateservice.mapper;

import com.campuswork.certificateservice.dto.CertificateResponse;
import com.campuswork.certificateservice.model.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateResponse toResponse(Certificate entity) {
        return CertificateResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .jobId(entity.getJobId())
                .issuedBy(entity.getIssuedBy())
                .title(entity.getTitle())
                .totalHours(entity.getTotalHours())
                .certificateNumber(entity.getCertificateNumber())
                .issueDate(entity.getIssueDate())
                .build();
    }
}