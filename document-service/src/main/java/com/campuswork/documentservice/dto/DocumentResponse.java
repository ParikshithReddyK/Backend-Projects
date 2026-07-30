package com.campuswork.documentservice.dto;

import com.campuswork.documentservice.model.DocumentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DocumentResponse {
    private Long id;
    private Long studentId;
    private DocumentType documentType;
    private String fileName;
    private String contentType;
    private boolean verified;
    private Long verifiedBy;
    private LocalDateTime uploadedAt;
}