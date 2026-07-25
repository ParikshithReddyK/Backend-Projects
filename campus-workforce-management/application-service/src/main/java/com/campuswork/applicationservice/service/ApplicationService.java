package com.campuswork.applicationservice.service;

import com.campuswork.applicationservice.dto.ApplicationResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(Long jobId, Long studentId, String bearerToken);
    List<ApplicationResponse> getMyApplications(Long studentId);
    List<ApplicationResponse> getApplicationsForJob(Long jobId, Long requesterId, boolean isAdmin, String bearerToken);
    ApplicationResponse updateStatus(Long applicationId, String newStatus, Long requesterId, boolean isAdmin, String bearerToken);
}