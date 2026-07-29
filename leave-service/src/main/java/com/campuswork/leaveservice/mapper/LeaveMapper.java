package com.campuswork.leaveservice.mapper;

import com.campuswork.leaveservice.dto.LeaveRequestDto;
import com.campuswork.leaveservice.dto.LeaveResponse;
import com.campuswork.leaveservice.model.LeaveRequest;
import org.springframework.stereotype.Component;

@Component
public class LeaveMapper {

    public LeaveRequest toEntity(LeaveRequestDto dto, Long studentId) {
        return LeaveRequest.builder()
                .studentId(studentId)
                .jobId(dto.getJobId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .reason(dto.getReason())
                .build();
    }

    public LeaveResponse toResponse(LeaveRequest entity) {
        return LeaveResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .jobId(entity.getJobId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .build();
    }
}