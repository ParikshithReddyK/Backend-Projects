package com.campuswork.leaveservice.service.impl;

import com.campuswork.leaveservice.client.JobServiceClient;
import com.campuswork.leaveservice.dto.JobDto;
import com.campuswork.leaveservice.dto.LeaveRequestDto;
import com.campuswork.leaveservice.dto.LeaveResponse;
import com.campuswork.leaveservice.mapper.LeaveMapper;
import com.campuswork.leaveservice.model.LeaveRequest;
import com.campuswork.leaveservice.model.LeaveStatus;
import com.campuswork.leaveservice.repository.LeaveRequestRepository;
import com.campuswork.leaveservice.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final JobServiceClient jobServiceClient;

    @Override
    public LeaveResponse requestLeave(LeaveRequestDto dto, Long studentId) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalStateException("End date cannot be before start date");
        }

        LeaveRequest entity = leaveMapper.toEntity(dto, studentId);
        LeaveRequest saved = leaveRepository.save(entity);
        return leaveMapper.toResponse(saved);
    }

    @Override
    public List<LeaveResponse> getMyLeaves(Long studentId) {
        return leaveRepository.findByStudentId(studentId).stream()
                .map(leaveMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveResponse> getLeavesForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken) {
        JobDto job = jobServiceClient.getJob(jobId, bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(requesterId)) {
            throw new AccessDeniedException("You do not have access to this job's leave requests");
        }

        return leaveRepository.findByJobId(jobId).stream()
                .map(leaveMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveResponse updateStatus(Long leaveId, String newStatus, Long requesterId, boolean isAdmin, String bearerToken) {
        LeaveRequest leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalStateException("Leave request not found"));

        JobDto job = jobServiceClient.getJob(leave.getJobId(), bearerToken);
        if (!isAdmin && !job.getPostedBy().equals(requesterId)) {
            throw new AccessDeniedException("You do not have access to update this leave request");
        }

        LeaveStatus status;
        try {
            status = LeaveStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid status: " + newStatus);
        }

        leave.setStatus(status);
        LeaveRequest updated = leaveRepository.save(leave);
        return leaveMapper.toResponse(updated);
    }
}