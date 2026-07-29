package com.campuswork.jobservice.mapper;

import com.campuswork.jobservice.dto.CreateJobRequest;
import com.campuswork.jobservice.dto.JobResponse;
import com.campuswork.jobservice.model.Job;
import com.campuswork.jobservice.model.JobStatus;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toEntity(CreateJobRequest request, Long postedBy) {
        return Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .department(request.getDepartment())
                .hourlyRate(request.getHourlyRate())
                .hoursPerWeek(request.getHoursPerWeek())
                .postedBy(postedBy)
                .status(JobStatus.OPEN)
                .build();
    }

    public JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .department(job.getDepartment())
                .hourlyRate(job.getHourlyRate())
                .hoursPerWeek(job.getHoursPerWeek())
                .postedBy(job.getPostedBy())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .build();
    }
}