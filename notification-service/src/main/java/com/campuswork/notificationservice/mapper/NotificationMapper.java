package com.campuswork.notificationservice.mapper;

import com.campuswork.notificationservice.dto.CreateNotificationRequest;
import com.campuswork.notificationservice.dto.NotificationResponse;
import com.campuswork.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(CreateNotificationRequest dto) {
        return Notification.builder()
                .recipientId(dto.getRecipientId())
                .type(dto.getType())
                .message(dto.getMessage())
                .build();
    }

    public NotificationResponse toResponse(Notification entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .recipientId(entity.getRecipientId())
                .type(entity.getType())
                .message(entity.getMessage())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}