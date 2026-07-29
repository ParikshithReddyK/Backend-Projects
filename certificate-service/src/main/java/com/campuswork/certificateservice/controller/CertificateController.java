package com.campuswork.certificateservice.controller;

import com.campuswork.certificateservice.dto.CertificateResponse;
import com.campuswork.certificateservice.dto.IssueCertificateRequest;
import com.campuswork.certificateservice.security.JwtUtil;
import com.campuswork.certificateservice.service.CertificateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CertificateResponse> issueCertificate(@Valid @RequestBody IssueCertificateRequest dto) {
        Long issuerId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificateService.issueCertificate(dto, issuerId, isAdmin, extractToken()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CertificateResponse>> getMyCertificates() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(certificateService.getMyCertificates(studentId));
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}