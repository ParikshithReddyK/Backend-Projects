package com.campuswork.equipmentservice.mapper;

import com.campuswork.equipmentservice.dto.*;
import com.campuswork.equipmentservice.model.Equipment;
import com.campuswork.equipmentservice.model.EquipmentAssignment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public Equipment toEntity(CreateEquipmentRequest dto) {
        return Equipment.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .serialNumber(dto.getSerialNumber())
                .build();
    }

    public EquipmentResponse toResponse(Equipment entity) {
        return EquipmentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .serialNumber(entity.getSerialNumber())
                .status(entity.getStatus())
                .build();
    }

    public AssignmentResponse toResponse(EquipmentAssignment entity) {
        return AssignmentResponse.builder()
                .id(entity.getId())
                .equipmentId(entity.getEquipmentId())
                .studentId(entity.getStudentId())
                .assignedBy(entity.getAssignedBy())
                .status(entity.getStatus())
                .assignedAt(entity.getAssignedAt())
                .returnedAt(entity.getReturnedAt())
                .build();
    }
}