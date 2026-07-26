package com.campuswork.shiftservice.client;

import com.campuswork.shiftservice.dto.ApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApplicationServiceClient {

    private final RestClient applicationServiceRestClient;

    public ApplicationDto getApplication(Long applicationId, String bearerToken) {
        return applicationServiceRestClient.get()
                .uri("/api/applications/{id}", applicationId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(ApplicationDto.class);
    }
}