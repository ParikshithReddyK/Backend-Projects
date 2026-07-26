package com.campuswork.attendanceservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AttendanceResponse {
    private Long id;
    private Long shiftId;
    private Long studentId;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
}