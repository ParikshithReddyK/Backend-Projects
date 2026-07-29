package com.campuswork.leaveservice.dto;

import com.campuswork.leaveservice.model.LeaveStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LeaveResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
}