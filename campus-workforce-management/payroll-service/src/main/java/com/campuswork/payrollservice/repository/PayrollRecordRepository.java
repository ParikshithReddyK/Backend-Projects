package com.campuswork.payrollservice.repository;

import com.campuswork.payrollservice.model.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, Long> {
    List<PayrollRecord> findByStudentId(Long studentId);
}