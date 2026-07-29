package com.campuswork.jobservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateJobRequest {

    @Size(max = 150)
    private String title;

    private String description;

    @Size(max = 100)
    private String department;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal hourlyRate;

    @Min(1)
    @Max(40)
    private Integer hoursPerWeek;
}
