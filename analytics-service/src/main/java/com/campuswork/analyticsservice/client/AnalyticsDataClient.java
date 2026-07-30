package com.campuswork.analyticsservice.client;

import com.campuswork.analyticsservice.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalyticsDataClient {

    private final RestClient jobServiceRestClient;
    private final RestClient applicationServiceRestClient;
    private final RestClient attendanceServiceRestClient;
    private final RestClient payrollServiceRestClient;

    public List<JobDto> getAllJobs(String bearerToken) {
        return jobServiceRestClient.get()
                .uri("/api/jobs/browse")
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<JobDto>>() {});
    }

    public List<ApplicationDto> getApplicationsForJob(Long jobId, String bearerToken) {
        return applicationServiceRestClient.get()
                .uri("/api/applications/job/{jobId}", jobId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ApplicationDto>>() {});
    }

    public List<AttendanceDto> getStudentAttendance(Long studentId, String bearerToken) {
        return attendanceServiceRestClient.get()
                .uri("/api/attendance/student/{studentId}", studentId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AttendanceDto>>() {});
    }
}