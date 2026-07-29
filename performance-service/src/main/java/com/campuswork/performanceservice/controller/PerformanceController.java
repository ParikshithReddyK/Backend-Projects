package com.campuswork.performanceservice.controller;

import com.campuswork.performanceservice.dto.CreateReviewRequest;
import com.campuswork.performanceservice.dto.ReviewResponse;
import com.campuswork.performanceservice.security.JwtUtil;
import com.campuswork.performanceservice.service.PerformanceService;
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
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest dto) {
        Long reviewerId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(performanceService.createReview(dto, reviewerId, isAdmin, extractToken()));
    }

    @GetMapping("/reviews/mine")
    public ResponseEntity<List<ReviewResponse>> getMyReviews() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(performanceService.getMyReviews(studentId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @GetMapping("/reviews/job/{jobId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsForJob(@PathVariable Long jobId) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.ok(performanceService.getReviewsForJob(jobId, requesterId, isAdmin, extractToken()));
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