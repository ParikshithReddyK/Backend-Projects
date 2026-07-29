package com.campuswork.payrollservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class GeneratePayrollRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long jobId;
    @NotNull
    private LocalDate periodStart;
    @NotNull
    private LocalDate periodEnd;
}