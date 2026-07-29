package com.campuswork.shiftservice.dto;

import com.campuswork.shiftservice.model.ShiftStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShiftResponse {
    private Long id;
    private Long jobId;
    private Long studentId;
    private LocalDateTime shiftStart;
    private LocalDateTime shiftEnd;
    private ShiftStatus status;
}