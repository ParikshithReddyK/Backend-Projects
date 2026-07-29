package com.campuswork.notificationservice.controller;

import com.campuswork.notificationservice.dto.CreateNotificationRequest;
import com.campuswork.notificationservice.dto.NotificationResponse;
import com.campuswork.notificationservice.security.JwtUtil;
import com.campuswork.notificationservice.service.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.create(dto));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        Long recipientId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(notificationService.getMyNotifications(recipientId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        Long requesterId = jwtUtil.extractUserId(extractToken());
        return ResponseEntity.ok(notificationService.markAsRead(id, requesterId));
    }

    private String extractToken() {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").substring(7);
    }
}