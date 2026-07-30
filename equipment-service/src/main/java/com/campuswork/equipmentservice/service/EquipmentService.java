package com.campuswork.equipmentservice.service;

import com.campuswork.equipmentservice.dto.*;

import java.util.List;

public interface EquipmentService {
    EquipmentResponse addEquipment(CreateEquipmentRequest dto);
    List<EquipmentResponse> browseEquipment();
    AssignmentResponse assign(AssignEquipmentRequest dto, Long assignerId);
    AssignmentResponse returnEquipment(Long assignmentId);
    List<AssignmentResponse> getMyAssignments(Long studentId);
}