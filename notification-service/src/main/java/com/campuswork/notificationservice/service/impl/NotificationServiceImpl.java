package com.campuswork.notificationservice.service.impl;

import com.campuswork.notificationservice.dto.CreateNotificationRequest;
import com.campuswork.notificationservice.dto.NotificationResponse;
import com.campuswork.notificationservice.mapper.NotificationMapper;
import com.campuswork.notificationservice.model.Notification;
import com.campuswork.notificationservice.repository.NotificationRepository;
import com.campuswork.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse create(CreateNotificationRequest dto) {
        Notification entity = notificationMapper.toEntity(dto);
        Notification saved = notificationRepository.save(entity);
        return notificationMapper.toResponse(saved);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(Long recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationResponse markAsRead(Long id, Long requesterId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Notification not found"));

        if (!notification.getRecipientId().equals(requesterId)) {
            throw new AccessDeniedException("You can only mark your own notifications as read");
        }

        notification.setRead(true);
        Notification updated = notificationRepository.save(notification);
        return notificationMapper.toResponse(updated);
    }
}