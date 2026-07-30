package com.campuswork.analyticsservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayrollDto {
    private Long id;
    private BigDecimal totalPay;
}