package com.campuswork.performanceservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private Long studentId;
    private Long jobId;
    private Long reviewerId;
    private Integer rating;
    private String feedback;
    private LocalDate reviewPeriodStart;
    private LocalDate reviewPeriodEnd;
}