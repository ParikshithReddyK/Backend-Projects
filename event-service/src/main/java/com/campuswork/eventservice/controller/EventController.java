package com.campuswork.eventservice.controller;

import com.campuswork.eventservice.dto.CreateEventRequest;
import com.campuswork.eventservice.dto.EventResponse;
import com.campuswork.eventservice.dto.RegistrationResponse;
import com.campuswork.eventservice.security.JwtUtil;
import com.campuswork.eventservice.service.EventService;
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
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest dto) {
        Long creatorId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(dto, creatorId));
    }

    @GetMapping("/browse")
    public ResponseEntity<List<EventResponse>> browseEvents() {
        return ResponseEntity.ok(eventService.browseEvents());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{eventId}/register")
    public ResponseEntity<RegistrationResponse> register(@PathVariable Long eventId) {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.register(eventId, studentId));
    }

    @GetMapping("/registrations/mine")
    public ResponseEntity<List<RegistrationResponse>> getMyRegistrations() {
        Long studentId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(eventService.getMyRegistrations(studentId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'HR_MANAGER', 'ADMIN')")
    @PatchMapping("/registrations/{registrationId}/attended")
    public ResponseEntity<RegistrationResponse> markAttended(@PathVariable Long registrationId) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        boolean isAdmin = isAdmin();
        return ResponseEntity.ok(eventService.markAttended(registrationId, requesterId, isAdmin));
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