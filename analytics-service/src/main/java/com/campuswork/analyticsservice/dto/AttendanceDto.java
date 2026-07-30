package com.campuswork.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendanceDto {
    private Long id;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
}