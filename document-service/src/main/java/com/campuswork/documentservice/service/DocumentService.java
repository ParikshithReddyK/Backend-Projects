package com.campuswork.documentservice.service;

import com.campuswork.documentservice.dto.DocumentResponse;
import com.campuswork.documentservice.model.Document;
import com.campuswork.documentservice.model.DocumentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    DocumentResponse upload(MultipartFile file, DocumentType type, Long studentId) throws Exception;
    List<DocumentResponse> getMyDocuments(Long studentId);
    Document getDocumentForDownload(Long id, Long requesterId, boolean isStaff);
    DocumentResponse verify(Long id, Long verifierId);
}