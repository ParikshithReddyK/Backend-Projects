package com.campuswork.attendanceservice.service.impl;

import com.campuswork.attendanceservice.client.ShiftServiceClient;
import com.campuswork.attendanceservice.dto.AttendanceResponse;
import com.campuswork.attendanceservice.dto.ClockInRequest;
import com.campuswork.attendanceservice.dto.ShiftDto;
import com.campuswork.attendanceservice.mapper.AttendanceMapper;
import com.campuswork.attendanceservice.model.AttendanceRecord;
import com.campuswork.attendanceservice.repository.AttendanceRepository;
import com.campuswork.attendanceservice.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final ShiftServiceClient shiftServiceClient;

    @Override
    public AttendanceResponse clockIn(ClockInRequest request, Long studentId, String bearerToken) {
        if (attendanceRepository.findByStudentIdAndClockOutIsNull(studentId).isPresent()) {
            throw new IllegalStateException("You are already clocked in");
        }

        ShiftDto shift = shiftServiceClient.getShiftById(request.getShiftId(), bearerToken);

        if (!shift.getStudentId().equals(studentId)) {
            throw new IllegalStateException("This shift is not assigned to you");
        }

        AttendanceRecord record = AttendanceRecord.builder()
                .shiftId(shift.getId())
                .studentId(studentId)
                .clockIn(LocalDateTime.now())
                .build();

        AttendanceRecord saved = attendanceRepository.save(record);
        return attendanceMapper.toResponse(saved);
    }

    @Override
    public AttendanceResponse clockOut(Long studentId) {
        AttendanceRecord record = attendanceRepository.findByStudentIdAndClockOutIsNull(studentId)
                .orElseThrow(() -> new IllegalStateException("You are not currently clocked in"));

        record.setClockOut(LocalDateTime.now());
        AttendanceRecord updated = attendanceRepository.save(record);
        return attendanceMapper.toResponse(updated);
    }

    @Override
    public List<AttendanceResponse> getMyAttendance(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(attendanceMapper::toResponse)
                .collect(Collectors.toList());
    }
}