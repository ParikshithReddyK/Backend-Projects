package com.campuswork.performanceservice.service.impl;

import com.campuswork.performanceservice.client.JobServiceClient;
import com.campuswork.performanceservice.dto.CreateReviewRequest;
import com.campuswork.performanceservice.dto.JobDto;
import com.campuswork.performanceservice.dto.ReviewResponse;
import com.campuswork.performanceservice.mapper.ReviewMapper;
import com.campuswork.performanceservice.model.PerformanceReview;
import com.campuswork.performanceservice.repository.PerformanceReviewRepository;
import com.campuswork.performanceservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final JobServiceClient jobServiceClient;

    @Override
    public ReviewResponse createReview(CreateReviewRequest dto, Long reviewerId, boolean isAdmin, String bearerToken) {
        JobDto job = jobServiceClient.getJob(dto.getJobId(), bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(reviewerId)) {
            throw new AccessDeniedException("You can only review students on jobs you posted");
        }

        PerformanceReview entity = reviewMapper.toEntity(dto, reviewerId);
        PerformanceReview saved = reviewRepository.save(entity);
        return reviewMapper.toResponse(saved);
    }

    @Override
    public List<ReviewResponse> getMyReviews(Long studentId) {
        return reviewRepository.findByStudentId(studentId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponse> getReviewsForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken) {
        JobDto job = jobServiceClient.getJob(jobId, bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(requesterId)) {
            throw new AccessDeniedException("You do not have access to this job's reviews");
        }

        return reviewRepository.findByJobId(jobId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }
}