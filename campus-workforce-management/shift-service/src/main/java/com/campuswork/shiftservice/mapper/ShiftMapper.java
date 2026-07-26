package com.campuswork.shiftservice.mapper;

import com.campuswork.shiftservice.dto.ShiftResponse;
import com.campuswork.shiftservice.model.Shift;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {
    public ShiftResponse toResponse(Shift shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .jobId(shift.getJobId())
                .studentId(shift.getStudentId())
                .shiftStart(shift.getShiftStart())
                .shiftEnd(shift.getShiftEnd())
                .status(shift.getStatus())
                .build();
    }
}