package com.campuswork.analyticsservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class AnalyticsSummaryResponse {
    private long totalJobs;
    private long openJobs;
    private long totalApplications;
    private Map<String, Long> applicationsByStatus;
    private double totalHoursLogged;
    private BigDecimal totalPayrollPaid;
}