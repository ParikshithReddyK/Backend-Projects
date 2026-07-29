package com.campuswork.certificateservice.client;

import com.campuswork.certificateservice.dto.AttendanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceServiceClient {

    private final RestClient attendanceServiceRestClient;

    public List<AttendanceDto> getStudentAttendance(Long studentId, String bearerToken) {
        return attendanceServiceRestClient.get()
                .uri("/api/attendance/student/{studentId}", studentId)
                .header("Authorization", "Bearer " + bearerToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AttendanceDto>>() {});
    }
}