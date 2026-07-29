package com.campuswork.certificateservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class CertificateResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private Long issuedBy;
    private String title;
    private BigDecimal totalHours;
    private String certificateNumber;
    private LocalDate issueDate;
}