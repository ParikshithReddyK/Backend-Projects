package com.campuswork.shiftservice.service.impl;

import com.campuswork.shiftservice.client.ApplicationServiceClient;
import com.campuswork.shiftservice.dto.ApplicationDto;
import com.campuswork.shiftservice.dto.CreateShiftRequest;
import com.campuswork.shiftservice.dto.ShiftResponse;
import com.campuswork.shiftservice.mapper.ShiftMapper;
import com.campuswork.shiftservice.model.Shift;
import com.campuswork.shiftservice.repository.ShiftRepository;
import com.campuswork.shiftservice.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final ApplicationServiceClient applicationServiceClient;

    @Override
    public ShiftResponse createShift(CreateShiftRequest request, String bearerToken) {
        ApplicationDto application = applicationServiceClient.getApplication(request.getApplicationId(), bearerToken);

        if (!"ACCEPTED".equals(application.getStatus())) {
            throw new IllegalStateException("Cannot schedule a shift for a non-accepted application");
        }

        Shift shift = Shift.builder()
                .jobId(application.getJobId())
                .studentId(application.getStudentId())
                .shiftStart(request.getShiftStart())
                .shiftEnd(request.getShiftEnd())
                .build();

        Shift saved = shiftRepository.save(shift);
        return shiftMapper.toResponse(saved);
    }

    @Override
    public List<ShiftResponse> getMyShifts(Long studentId) {
        return shiftRepository.findByStudentId(studentId).stream()
                .map(shiftMapper::toResponse)
                .collect(Collectors.toList());
    }
}