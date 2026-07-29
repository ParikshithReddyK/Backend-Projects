package com.campuswork.performanceservice.service;

import com.campuswork.performanceservice.dto.CreateReviewRequest;
import com.campuswork.performanceservice.dto.ReviewResponse;

import java.util.List;

public interface PerformanceService {
    ReviewResponse createReview(CreateReviewRequest dto, Long reviewerId, boolean isAdmin, String bearerToken);
    List<ReviewResponse> getMyReviews(Long studentId);
    List<ReviewResponse> getReviewsForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken);
}