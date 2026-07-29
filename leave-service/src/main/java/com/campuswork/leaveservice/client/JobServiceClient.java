package com.campuswork.leaveservice.client;

import com.campuswork.leaveservice.dto.JobDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class JobServiceClient {

    private final RestClient jobServiceRestClient;

    public JobDto getJob(Long jobId, String bearerToken) {
        return jobServiceRestClient.get()
                .uri("/api/jobs/{id}", jobId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(JobDto.class);
    }
}