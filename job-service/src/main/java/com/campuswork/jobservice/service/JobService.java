package com.campuswork.jobservice.service;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.dto.UpdateJobRequest;

import java.util.List;

public interface JobService {
    JobResponse createJob(CreateJobRequest request, Long postedBy);
    List<JobResponse> getOpenJobs();
    List<JobResponse> getMyJobs(Long postedBy);
    JobResponse getJobById(Long id);
    JobResponse updateJob(Long id, UpdateJobRequest request, Long currentUserId, boolean isAdmin);
    void closeJob(Long id, Long currentUserId, boolean isAdmin);
}