package com.campuswork.payrollservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceDto {
    private Long id;
    private Long shiftId;
    private Long studentId;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
}