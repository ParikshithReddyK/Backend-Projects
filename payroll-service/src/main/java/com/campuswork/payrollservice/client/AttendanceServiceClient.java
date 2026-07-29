package com.campuswork.payrollservice.client;

import com.campuswork.payrollservice.dto.AttendanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceServiceClient {

    private final RestClient attendanceServiceRestClient;

    public List<AttendanceDto> getAttendanceForStudent(Long studentId, String bearerToken) {
        return attendanceServiceRestClient.get()
                .uri("/api/attendance/student/{studentId}", studentId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<AttendanceDto>>() {});
    }
}