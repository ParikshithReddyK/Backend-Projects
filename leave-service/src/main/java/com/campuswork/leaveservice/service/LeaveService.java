package com.campuswork.leaveservice.service;

import com.campuswork.leaveservice.dto.LeaveRequestDto;
import com.campuswork.leaveservice.dto.LeaveResponse;

import java.util.List;

public interface LeaveService {
    LeaveResponse requestLeave(LeaveRequestDto dto, Long studentId);
    List<LeaveResponse> getMyLeaves(Long studentId);
    List<LeaveResponse> getLeavesForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken);
    LeaveResponse updateStatus(Long leaveId, String newStatus, Long requesterId, boolean isAdmin, String bearerToken);
}