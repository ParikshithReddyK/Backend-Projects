package com.campuswork.jobservice.repository;

import com.campuswork.jobservice.model.Job;
import com.campuswork.jobservice.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByPostedBy(Long postedBy);
}