package com.campuswork.applicationservice.service.impl;

import com.campuswork.applicationservice.client.JobServiceClient;
import com.campuswork.applicationservice.dto.ApplicationResponse;
import com.campuswork.applicationservice.dto.JobDto;
import com.campuswork.applicationservice.mapper.ApplicationMapper;
import com.campuswork.applicationservice.model.ApplicationStatus;
import com.campuswork.applicationservice.model.JobApplication;
import com.campuswork.applicationservice.repository.JobApplicationRepository;
import com.campuswork.applicationservice.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final JobServiceClient jobServiceClient;

    @Override
    public ApplicationResponse apply(Long jobId, Long studentId, String bearerToken) {
        JobDto job;
        try {
            job = jobServiceClient.getJob(jobId, bearerToken);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalStateException("Job not found with id: " + jobId);
        }

        if (!"OPEN".equals(job.getStatus())) {
            throw new IllegalStateException("Job is not open for applications");
        }

        if (applicationRepository.existsByJobIdAndStudentId(jobId, studentId)) {
            throw new IllegalStateException("You have already applied to this job");
        }

        JobApplication application = applicationMapper.toEntity(jobId, studentId);
        JobApplication saved = applicationRepository.save(application);
        return applicationMapper.toResponse(saved);
    }

    @Override
    public List<ApplicationResponse> getMyApplications(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream()
                .map(applicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken) {
        JobDto job = jobServiceClient.getJob(jobId, bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(requesterId)) {
            throw new AccessDeniedException("You do not have access to this job's applications");
        }

        return applicationRepository.findByJobId(jobId).stream()
                .map(applicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, String newStatus, Long requesterId, boolean isAdmin, String bearerToken) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        JobDto job = jobServiceClient.getJob(application.getJobId(), bearerToken);

        if (!isAdmin && !job.getPostedBy().equals(requesterId)) {
            throw new AccessDeniedException("You do not have access to update this application");
        }

        ApplicationStatus status;
        try {
            status = ApplicationStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid status: " + newStatus);
        }

        application.setStatus(status);
        JobApplication updated = applicationRepository.save(application);

        return applicationMapper.toResponse(updated);
    }

    // ===================== NEW METHOD =====================

    @Override
    public ApplicationResponse getById(Long id) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Application not found"));

        return applicationMapper.toResponse(application);
    }
}