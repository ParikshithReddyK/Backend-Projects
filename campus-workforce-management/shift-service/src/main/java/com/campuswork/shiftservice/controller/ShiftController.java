package com.campuswork.shiftservice.controller;

import com.campuswork.shiftservice.dto.CreateShiftRequest;
import com.campuswork.shiftservice.dto.ShiftResponse;
import com.campuswork.shiftservice.security.JwtUtil;
import com.campuswork.shiftservice.service.ShiftService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody CreateShiftRequest request) {
        String token = extractToken();
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.createShift(request, token));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ShiftResponse>> getMyShifts() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(shiftService.getMyShifts(studentId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}