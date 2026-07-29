package com.campuswork.leaveservice.repository;

import com.campuswork.leaveservice.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStudentId(Long studentId);
    List<LeaveRequest> findByJobId(Long jobId);
}