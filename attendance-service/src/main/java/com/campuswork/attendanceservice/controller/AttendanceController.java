package com.campuswork.attendanceservice.controller;

import com.campuswork.attendanceservice.dto.AttendanceResponse;
import com.campuswork.attendanceservice.dto.ClockInRequest;
import com.campuswork.attendanceservice.security.JwtUtil;
import com.campuswork.attendanceservice.service.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final JwtUtil jwtUtil;

    @PostMapping("/clock-in")
    public ResponseEntity<AttendanceResponse> clockIn(@Valid @RequestBody ClockInRequest request) {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.clockIn(request, studentId, extractToken()));
    }

    @PatchMapping("/clock-out")
    public ResponseEntity<AttendanceResponse> clockOut() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(attendanceService.clockOut(studentId));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(attendanceService.getMyAttendance(studentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(studentId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}