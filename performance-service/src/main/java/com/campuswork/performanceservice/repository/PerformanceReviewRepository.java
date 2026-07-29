package com.campuswork.performanceservice.repository;

import com.campuswork.performanceservice.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    List<PerformanceReview> findByStudentId(Long studentId);
    List<PerformanceReview> findByJobId(Long jobId);
}