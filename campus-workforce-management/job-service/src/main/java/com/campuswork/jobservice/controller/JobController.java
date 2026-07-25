package com.campuswork.jobservice.controller;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.dto.UpdateJobRequest;
import com.campuswork.jobservice.security.JwtUtil;
import com.campuswork.jobservice.service.JobService;
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
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request) {
        Long postedBy = getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(request, postedBy));
    }

    @GetMapping("/browse")
    public ResponseEntity<List<JobResponse>> browseJobs() {
        return ResponseEntity.ok(jobService.getOpenJobs());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<JobResponse>> getMyJobs() {
        Long postedBy = getCurrentUserId();
        return ResponseEntity.ok(jobService.getMyJobs(postedBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long id, @Valid @RequestBody UpdateJobRequest request) {
        Long currentUserId = getCurrentUserId();
        boolean isAdmin = isAdmin();
        return ResponseEntity.ok(jobService.updateJob(id, request, currentUserId, isAdmin));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> closeJob(@PathVariable Long id) {
        Long currentUserId = getCurrentUserId();
        boolean isAdmin = isAdmin();
        jobService.closeJob(id, currentUserId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        String token = extractToken();
        return jwtUtil.extractUserId(token);
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