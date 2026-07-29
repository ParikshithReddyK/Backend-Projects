package com.campuswork.eventservice.dto;

import com.campuswork.eventservice.model.RegistrationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegistrationResponse {
    private Long id;
    private Long eventId;
    private Long studentId;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
}