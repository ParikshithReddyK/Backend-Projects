package com.campuswork.equipmentservice.controller;

import com.campuswork.equipmentservice.dto.*;
import com.campuswork.equipmentservice.security.JwtUtil;
import com.campuswork.equipmentservice.service.EquipmentService;
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
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<EquipmentResponse> addEquipment(@Valid @RequestBody CreateEquipmentRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.addEquipment(dto));
    }

    @GetMapping("/browse")
    public ResponseEntity<List<EquipmentResponse>> browseEquipment() {
        return ResponseEntity.ok(equipmentService.browseEquipment());
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<AssignmentResponse> assign(@Valid @RequestBody AssignEquipmentRequest dto) {
        Long assignerId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.assign(dto, assignerId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/assignments/{id}/return")
    public ResponseEntity<AssignmentResponse> returnEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(equipmentService.returnEquipment(id));
    }

    @GetMapping("/assignments/mine")
    public ResponseEntity<List<AssignmentResponse>> getMyAssignments() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(equipmentService.getMyAssignments(studentId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}