package com.campuswork.jobservice.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateJobRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 100)
    private String department;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal hourlyRate;

    @NotNull
    @Min(1)
    @Max(40)
    private Integer hoursPerWeek;
}