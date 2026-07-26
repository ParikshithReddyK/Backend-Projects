package com.campuswork.payrollservice.mapper;

import com.campuswork.payrollservice.dto.PayrollResponse;
import com.campuswork.payrollservice.model.PayrollRecord;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {
    public PayrollResponse toResponse(PayrollRecord record) {
        return PayrollResponse.builder()
                .id(record.getId())
                .studentId(record.getStudentId())
                .jobId(record.getJobId())
                .periodStart(record.getPeriodStart())
                .periodEnd(record.getPeriodEnd())
                .totalHours(record.getTotalHours())
                .hourlyRate(record.getHourlyRate())
                .totalPay(record.getTotalPay())
                .build();
    }
}