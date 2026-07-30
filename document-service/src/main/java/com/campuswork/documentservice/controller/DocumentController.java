package com.campuswork.documentservice.controller;

import com.campuswork.documentservice.dto.DocumentResponse;
import com.campuswork.documentservice.model.Document;
import com.campuswork.documentservice.model.DocumentType;
import com.campuswork.documentservice.security.JwtUtil;
import com.campuswork.documentservice.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") DocumentType type
    ) throws Exception {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.upload(file, type, studentId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<DocumentResponse>> getMyDocuments() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(documentService.getMyDocuments(studentId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        boolean isStaff = isStaff();
        Document document = documentService.getDocumentForDownload(id, requesterId, isStaff);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .body(document.getFileData());
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/{id}/verify")
    public ResponseEntity<DocumentResponse> verify(@PathVariable Long id) {
        Long verifierId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(documentService.verify(id, verifierId));
    }

    private boolean isStaff() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_HR_MANAGER")
                        || a.getAuthority().equals("ROLE_SUPERVISOR"));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}