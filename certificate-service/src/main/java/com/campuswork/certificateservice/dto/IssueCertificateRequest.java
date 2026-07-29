package com.campuswork.certificateservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueCertificateRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long jobId;
    @NotBlank
    private String title;
}