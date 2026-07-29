package com.campuswork.notificationservice.service;

import com.campuswork.notificationservice.dto.CreateNotificationRequest;
import com.campuswork.notificationservice.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    NotificationResponse create(CreateNotificationRequest dto);
    List<NotificationResponse> getMyNotifications(Long recipientId);
    NotificationResponse markAsRead(Long id, Long requesterId);
}