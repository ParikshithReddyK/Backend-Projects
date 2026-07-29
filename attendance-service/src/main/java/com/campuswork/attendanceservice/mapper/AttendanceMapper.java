package com.campuswork.attendanceservice.mapper;

import com.campuswork.attendanceservice.dto.AttendanceResponse;
import com.campuswork.attendanceservice.model.AttendanceRecord;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {
    public AttendanceResponse toResponse(AttendanceRecord record) {
        return AttendanceResponse.builder()
                .id(record.getId())
                .shiftId(record.getShiftId())
                .studentId(record.getStudentId())
                .clockIn(record.getClockIn())
                .clockOut(record.getClockOut())
                .build();
    }
}