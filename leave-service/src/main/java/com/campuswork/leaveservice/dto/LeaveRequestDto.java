package com.campuswork.leaveservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequestDto {
    @NotNull
    private Long jobId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotBlank
    private String reason;
}