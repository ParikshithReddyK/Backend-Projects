package com.campuswork.payrollservice.service;

import com.campuswork.payrollservice.dto.GeneratePayrollRequest;
import com.campuswork.payrollservice.dto.PayrollResponse;

import java.util.List;

public interface PayrollService {
    PayrollResponse generatePayroll(GeneratePayrollRequest request, String bearerToken);
    List<PayrollResponse> getMyPayroll(Long studentId);
}