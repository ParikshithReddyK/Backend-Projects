package com.campuswork.analyticsservice.controller;

import com.campuswork.analyticsservice.dto.AnalyticsSummaryResponse;
import com.campuswork.analyticsservice.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @GetMapping("/jobs-overview")
    public ResponseEntity<AnalyticsSummaryResponse> getJobsOverview() {
        return ResponseEntity.ok(analyticsService.getJobsOverview(extractToken()));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}