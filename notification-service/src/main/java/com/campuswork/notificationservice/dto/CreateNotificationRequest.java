package com.campuswork.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotificationRequest {
    @NotNull
    private Long recipientId;
    @NotBlank
    private String type;
    @NotBlank
    private String message;
}