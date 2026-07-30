package com.campuswork.trainingservice.controller;

import com.campuswork.trainingservice.dto.*;
import com.campuswork.trainingservice.security.JwtUtil;
import com.campuswork.trainingservice.service.TrainingService;
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
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping("/modules")
    public ResponseEntity<ModuleResponse> createModule(@Valid @RequestBody CreateModuleRequest dto) {
        Long creatorId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.createModule(dto, creatorId));
    }

    @GetMapping("/modules/browse")
    public ResponseEntity<List<ModuleResponse>> browseModules() {
        return ResponseEntity.ok(trainingService.browseModules());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/modules/{moduleId}/enroll")
    public ResponseEntity<CompletionResponse> enroll(@PathVariable Long moduleId) {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingService.enroll(moduleId, studentId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/completions/{completionId}/complete")
    public ResponseEntity<CompletionResponse> complete(@PathVariable Long completionId, @Valid @RequestBody CompleteTrainingRequest dto) {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(trainingService.completeTraining(completionId, dto, studentId));
    }

    @GetMapping("/completions/mine")
    public ResponseEntity<List<CompletionResponse>> getMyCompletions() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(trainingService.getMyCompletions(studentId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}