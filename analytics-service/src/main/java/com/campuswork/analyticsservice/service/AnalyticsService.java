package com.campuswork.analyticsservice.service;

import com.campuswork.analyticsservice.dto.AnalyticsSummaryResponse;

public interface AnalyticsService {
    AnalyticsSummaryResponse getJobsOverview(String bearerToken);
}