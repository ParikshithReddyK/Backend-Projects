package com.campuswork.jobservice.service;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;

import java.util.List;

public interface JobService {
    JobResponse createJob(CreateJobRequest request, Long postedBy);
    List<JobResponse> getOpenJobs();
    List<JobResponse> getMyJobs(Long postedBy);
}
