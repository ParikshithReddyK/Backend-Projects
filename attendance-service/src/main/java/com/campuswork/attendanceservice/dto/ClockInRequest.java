package com.campuswork.attendanceservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClockInRequest {
    @NotNull
    private Long shiftId;
}