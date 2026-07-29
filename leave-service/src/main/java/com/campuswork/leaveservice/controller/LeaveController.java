package com.campuswork.leaveservice.controller;

import com.campuswork.leaveservice.dto.LeaveRequestDto;
import com.campuswork.leaveservice.dto.LeaveResponse;
import com.campuswork.leaveservice.security.JwtUtil;
import com.campuswork.leaveservice.service.LeaveService;
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
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<LeaveResponse> requestLeave(@Valid @RequestBody LeaveRequestDto dto) {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveService.requestLeave(dto, studentId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<LeaveResponse>> getMyLeaves() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(leaveService.getMyLeaves(studentId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<LeaveResponse>> getLeavesForJob(@PathVariable Long jobId) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.ok(leaveService.getLeavesForJob(jobId, requesterId, isAdmin, extractToken()));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveResponse> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.ok(leaveService.updateStatus(id, status, requesterId, isAdmin, extractToken()));
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