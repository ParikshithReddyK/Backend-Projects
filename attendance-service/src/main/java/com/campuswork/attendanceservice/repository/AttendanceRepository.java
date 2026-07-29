package com.campuswork.attendanceservice.repository;

import com.campuswork.attendanceservice.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByStudentId(Long studentId);
    Optional<AttendanceRecord> findByStudentIdAndClockOutIsNull(Long studentId);
}