package com.campuswork.shiftservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateShiftRequest {
    @NotNull
    private Long applicationId;
    @NotNull
    private LocalDateTime shiftStart;
    @NotNull
    private LocalDateTime shiftEnd;
}