package com.campuswork.performanceservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateReviewRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long jobId;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    @NotBlank
    private String feedback;
    @NotNull
    private LocalDate reviewPeriodStart;
    @NotNull
    private LocalDate reviewPeriodEnd;
}