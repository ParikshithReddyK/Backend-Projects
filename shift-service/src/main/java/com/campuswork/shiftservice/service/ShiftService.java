package com.campuswork.shiftservice.service;

import com.campuswork.shiftservice.dto.CreateShiftRequest;
import com.campuswork.shiftservice.dto.ShiftResponse;

import java.util.List;

public interface ShiftService {
    ShiftResponse createShift(CreateShiftRequest request, String bearerToken);
    List<ShiftResponse> getMyShifts(Long studentId);
    ShiftResponse getById(Long id);
}