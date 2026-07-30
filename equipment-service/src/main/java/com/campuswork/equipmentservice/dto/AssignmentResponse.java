package com.campuswork.equipmentservice.dto;

import com.campuswork.equipmentservice.model.AssignmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AssignmentResponse {
    private Long id;
    private Long equipmentId;
    private Long studentId;
    private Long assignedBy;
    private AssignmentStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime returnedAt;
}