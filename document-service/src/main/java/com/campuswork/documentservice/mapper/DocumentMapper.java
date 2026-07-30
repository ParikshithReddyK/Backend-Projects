package com.campuswork.documentservice.mapper;

import com.campuswork.documentservice.dto.DocumentResponse;
import com.campuswork.documentservice.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(Document entity) {
        return DocumentResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .documentType(entity.getDocumentType())
                .fileName(entity.getFileName())
                .contentType(entity.getContentType())
                .verified(entity.isVerified())
                .verifiedBy(entity.getVerifiedBy())
                .uploadedAt(entity.getUploadedAt())
                .build();
    }
}