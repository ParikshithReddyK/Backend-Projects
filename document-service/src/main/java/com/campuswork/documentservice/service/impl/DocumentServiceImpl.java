package com.campuswork.documentservice.service.impl;

import com.campuswork.documentservice.dto.DocumentResponse;
import com.campuswork.documentservice.mapper.DocumentMapper;
import com.campuswork.documentservice.model.Document;
import com.campuswork.documentservice.model.DocumentType;
import com.campuswork.documentservice.repository.DocumentRepository;
import com.campuswork.documentservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper mapper;

    @Override
    public DocumentResponse upload(MultipartFile file, DocumentType type, Long studentId) throws Exception {
        Document document = Document.builder()
                .studentId(studentId)
                .documentType(type)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileData(file.getBytes())
                .build();

        Document saved = documentRepository.save(document);
        return mapper.toResponse(saved);
    }

    @Override
    public List<DocumentResponse> getMyDocuments(Long studentId) {
        return documentRepository.findByStudentId(studentId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Document getDocumentForDownload(Long id, Long requesterId, boolean isStaff) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Document not found"));

        if (!isStaff && !document.getStudentId().equals(requesterId)) {
            throw new AccessDeniedException("You can only access your own documents");
        }

        return document;
    }

    @Override
    public DocumentResponse verify(Long id, Long verifierId) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Document not found"));

        document.setVerified(true);
        document.setVerifiedBy(verifierId);

        Document updated = documentRepository.save(document);
        return mapper.toResponse(updated);
    }
}