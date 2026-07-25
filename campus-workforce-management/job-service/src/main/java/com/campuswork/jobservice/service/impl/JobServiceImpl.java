package com.campuswork.jobservice.service.impl;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.dto.UpdateJobRequest;
import com.campuswork.jobservice.mapper.JobMapper;
import com.campuswork.jobservice.model.Job;
import com.campuswork.jobservice.model.JobStatus;
import com.campuswork.jobservice.repository.JobRepository;
import com.campuswork.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public JobResponse createJob(CreateJobRequest request, Long postedBy) {
        Job job = jobMapper.toEntity(request, postedBy);
        Job saved = jobRepository.save(job);
        return jobMapper.toResponse(saved);
    }

    @Override
    public List<JobResponse> getOpenJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN).stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobResponse> getMyJobs(Long postedBy) {
        return jobRepository.findByPostedBy(postedBy).stream()
                .map(jobMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found with id: " + id));
        return jobMapper.toResponse(job);
    }

    @Override
    public JobResponse updateJob(Long id, UpdateJobRequest request, Long currentUserId, boolean isAdmin) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found with id: " + id));

        if (!isAdmin && !job.getPostedBy().equals(currentUserId)) {
            throw new AccessDeniedException("You can only update jobs you posted");
        }

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getDepartment() != null) job.setDepartment(request.getDepartment());
        if (request.getHourlyRate() != null) job.setHourlyRate(request.getHourlyRate());
        if (request.getHoursPerWeek() != null) job.setHoursPerWeek(request.getHoursPerWeek());

        Job updated = jobRepository.save(job);
        return jobMapper.toResponse(updated);
    }

    @Override
    public void closeJob(Long id, Long currentUserId, boolean isAdmin) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found with id: " + id));

        if (!isAdmin && !job.getPostedBy().equals(currentUserId)) {
            throw new AccessDeniedException("You can only close jobs you posted");
        }

        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);
    }
}