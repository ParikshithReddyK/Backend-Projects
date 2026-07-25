package com.campuswork.jobservice.service.impl;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.mapper.JobMapper;
import com.campuswork.jobservice.model.Job;
import com.campuswork.jobservice.model.JobStatus;
import com.campuswork.jobservice.repository.JobRepository;
import com.campuswork.jobservice.service.JobService;
import lombok.RequiredArgsConstructor;
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
}