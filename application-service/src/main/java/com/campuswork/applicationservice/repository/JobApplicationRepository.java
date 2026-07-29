package com.campuswork.applicationservice.repository;

import com.campuswork.applicationservice.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByStudentId(Long studentId);
    List<JobApplication> findByJobId(Long jobId);
    Optional<JobApplication> findByJobIdAndStudentId(Long jobId, Long studentId);
    boolean existsByJobIdAndStudentId(Long jobId, Long studentId);
}