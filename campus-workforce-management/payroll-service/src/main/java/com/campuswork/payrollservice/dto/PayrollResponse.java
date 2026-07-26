package com.campuswork.payrollservice.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class PayrollResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal totalHours;
    private BigDecimal hourlyRate;
    private BigDecimal totalPay;
}