package com.campuswork.analyticsservice.service.impl;

import com.campuswork.analyticsservice.client.AnalyticsDataClient;
import com.campuswork.analyticsservice.dto.AnalyticsSummaryResponse;
import com.campuswork.analyticsservice.dto.ApplicationDto;
import com.campuswork.analyticsservice.dto.JobDto;
import com.campuswork.analyticsservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsDataClient dataClient;

    @Override
    public AnalyticsSummaryResponse getJobsOverview(String bearerToken) {
        List<JobDto> jobs = dataClient.getAllJobs(bearerToken);

        long totalJobs = jobs.size();
        long openJobs = jobs.stream().filter(j -> "OPEN".equals(j.getStatus())).count();

        List<ApplicationDto> allApplications = jobs.stream()
                .flatMap(job -> {
                    try {
                        return dataClient.getApplicationsForJob(job.getId(), bearerToken).stream();
                    } catch (Exception e) {
                        return List.<ApplicationDto>of().stream();
                    }
                })
                .collect(Collectors.toList());

        Map<String, Long> byStatus = allApplications.stream()
                .collect(Collectors.groupingBy(ApplicationDto::getStatus, Collectors.counting()));

        return AnalyticsSummaryResponse.builder()
                .totalJobs(totalJobs)
                .openJobs(openJobs)
                .totalApplications(allApplications.size())
                .applicationsByStatus(byStatus)
                .totalHoursLogged(0.0)
                .totalPayrollPaid(BigDecimal.ZERO)
                .build();
    }
}