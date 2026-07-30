package com.campuswork.trainingservice.dto;

import com.campuswork.trainingservice.model.CompletionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CompletionResponse {
    private Long id;
    private Long moduleId;
    private Long studentId;
    private CompletionStatus status;
    private Integer score;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}