package com.campuswork.attendanceservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShiftDto {
    private Long id;
    private Long jobId;
    private Long studentId;
}