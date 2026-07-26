package com.campuswork.attendanceservice.service;

import com.campuswork.attendanceservice.dto.AttendanceResponse;
import com.campuswork.attendanceservice.dto.ClockInRequest;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse clockIn(ClockInRequest request, Long studentId, String bearerToken);
    AttendanceResponse clockOut(Long studentId);
    List<AttendanceResponse> getMyAttendance(Long studentId);
}