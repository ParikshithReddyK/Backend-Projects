package com.campuswork.performanceservice.mapper;

import com.campuswork.performanceservice.dto.CreateReviewRequest;
import com.campuswork.performanceservice.dto.ReviewResponse;
import com.campuswork.performanceservice.model.PerformanceReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public PerformanceReview toEntity(CreateReviewRequest dto, Long reviewerId) {
        return PerformanceReview.builder()
                .studentId(dto.getStudentId())
                .jobId(dto.getJobId())
                .reviewerId(reviewerId)
                .rating(dto.getRating())
                .feedback(dto.getFeedback())
                .reviewPeriodStart(dto.getReviewPeriodStart())
                .reviewPeriodEnd(dto.getReviewPeriodEnd())
                .build();
    }

    public ReviewResponse toResponse(PerformanceReview entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .jobId(entity.getJobId())
                .reviewerId(entity.getReviewerId())
                .rating(entity.getRating())
                .feedback(entity.getFeedback())
                .reviewPeriodStart(entity.getReviewPeriodStart())
                .reviewPeriodEnd(entity.getReviewPeriodEnd())
                .build();
    }
}