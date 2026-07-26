package com.campuswork.attendanceservice.client;

import com.campuswork.attendanceservice.dto.ShiftDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ShiftServiceClient {

    private final RestClient shiftServiceRestClient;

    public ShiftDto getShiftById(Long shiftId, String bearerToken) {
        // Note: Shift Service does not yet expose GET /api/shifts/{id} — add it there first.
        return shiftServiceRestClient.get()
                .uri("/api/shifts/{id}", shiftId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(ShiftDto.class);
    }
}