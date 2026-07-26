package com.campuswork.payrollservice.service.impl;

import com.campuswork.payrollservice.client.AttendanceServiceClient;
import com.campuswork.payrollservice.client.JobServiceClient;
import com.campuswork.payrollservice.dto.AttendanceDto;
import com.campuswork.payrollservice.dto.GeneratePayrollRequest;
import com.campuswork.payrollservice.dto.JobDto;
import com.campuswork.payrollservice.dto.PayrollResponse;
import com.campuswork.payrollservice.mapper.PayrollMapper;
import com.campuswork.payrollservice.model.PayrollRecord;
import com.campuswork.payrollservice.repository.PayrollRecordRepository;
import com.campuswork.payrollservice.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRecordRepository payrollRepository;
    private final PayrollMapper payrollMapper;
    private final JobServiceClient jobServiceClient;
    private final AttendanceServiceClient attendanceServiceClient;

    @Override
    public PayrollResponse generatePayroll(GeneratePayrollRequest request, String bearerToken) {
        JobDto job = jobServiceClient.getJob(request.getJobId(), bearerToken);
        List<AttendanceDto> records = attendanceServiceClient.getAttendanceForStudent(request.getStudentId(), bearerToken);

        BigDecimal totalMinutes = records.stream()
                .filter(r -> r.getClockOut() != null)
                .filter(r -> isWithinPeriod(r.getClockIn().toLocalDate(), request.getPeriodStart(), request.getPeriodEnd()))
                .map(r -> BigDecimal.valueOf(Duration.between(r.getClockIn(), r.getClockOut()).toMinutes()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHours = totalMinutes.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal totalPay = totalHours.multiply(job.getHourlyRate()).setScale(2, RoundingMode.HALF_UP);

        PayrollRecord record = PayrollRecord.builder()
                .studentId(request.getStudentId())
                .jobId(request.getJobId())
                .periodStart(request.getPeriodStart())
                .periodEnd(request.getPeriodEnd())
                .totalHours(totalHours)
                .hourlyRate(job.getHourlyRate())
                .totalPay(totalPay)
                .build();

        PayrollRecord saved = payrollRepository.save(record);
        return payrollMapper.toResponse(saved);
    }

    @Override
    public List<PayrollResponse> getMyPayroll(Long studentId) {
        return payrollRepository.findByStudentId(studentId).stream()
                .map(payrollMapper::toResponse)
                .collect(Collectors.toList());
    }

    private boolean isWithinPeriod(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}