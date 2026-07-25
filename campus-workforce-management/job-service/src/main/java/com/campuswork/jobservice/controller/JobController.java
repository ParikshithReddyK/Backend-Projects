package com.campuswork.jobservice.controller;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.security.JwtUtil;
import com.campuswork.jobservice.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
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

    private Long getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        return jwtUtil.extractUserId(token);
    }
}