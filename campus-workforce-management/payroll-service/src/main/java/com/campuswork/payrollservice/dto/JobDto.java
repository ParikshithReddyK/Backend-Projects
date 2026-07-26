package com.campuswork.payrollservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class JobDto {
    private Long id;
    private BigDecimal hourlyRate;
}