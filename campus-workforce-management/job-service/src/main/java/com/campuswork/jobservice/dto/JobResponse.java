package com.campuswork.jobservice.dto;

import com.campuswork.jobservice.model.JobStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String department;
    private BigDecimal hourlyRate;
    private Integer hoursPerWeek;
    private Long postedBy;
    private JobStatus status;
    private LocalDateTime createdAt;
}