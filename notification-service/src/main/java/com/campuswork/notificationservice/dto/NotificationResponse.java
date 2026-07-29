package com.campuswork.notificationservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long recipientId;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}