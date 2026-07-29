package com.campuswork.payrollservice.controller;

import com.campuswork.payrollservice.dto.GeneratePayrollRequest;
import com.campuswork.payrollservice.dto.PayrollResponse;
import com.campuswork.payrollservice.security.JwtUtil;
import com.campuswork.payrollservice.service.PayrollService;
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
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    @PostMapping("/generate")
    public ResponseEntity<PayrollResponse> generate(@Valid @RequestBody GeneratePayrollRequest request) {
        String token = extractToken();
        return ResponseEntity.status(HttpStatus.CREATED).body(payrollService.generatePayroll(request, token));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<PayrollResponse>> getMyPayroll() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(payrollService.getMyPayroll(studentId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}