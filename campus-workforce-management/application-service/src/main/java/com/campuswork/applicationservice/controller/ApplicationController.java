package com.campuswork.applicationservice.controller;

import com.campuswork.applicationservice.dto.ApplicationResponse;
import com.campuswork.applicationservice.dto.ApplyRequest;
import com.campuswork.applicationservice.security.JwtUtil;
import com.campuswork.applicationservice.service.ApplicationService;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<ApplicationResponse> apply(@Valid @RequestBody ApplyRequest request) {
        Long studentId = getCurrentUserId();
        String token = extractToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.apply(request.getJobId(), studentId, token));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications() {
        Long studentId = getCurrentUserId();
        return ResponseEntity.ok(applicationService.getMyApplications(studentId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsForJob(@PathVariable Long jobId) {
        Long requesterId = getCurrentUserId();
        boolean isAdmin = isAdmin();
        String token = extractToken();
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId, requesterId, isAdmin, token));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Long requesterId = getCurrentUserId();
        boolean isAdmin = isAdmin();
        String token = extractToken();
        return ResponseEntity.ok(applicationService.updateStatus(id, status, requesterId, isAdmin, token));
    }

    private Long getCurrentUserId() {
        return jwtUtil.extractUserId(extractToken());
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        String header = request.getHeader("Authorization");
        return header.substring(7);
    }
}