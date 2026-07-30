package com.campuswork.equipmentservice.service.impl;

import com.campuswork.equipmentservice.dto.*;
import com.campuswork.equipmentservice.mapper.EquipmentMapper;
import com.campuswork.equipmentservice.model.Equipment;
import com.campuswork.equipmentservice.model.EquipmentAssignment;
import com.campuswork.equipmentservice.model.EquipmentStatus;
import com.campuswork.equipmentservice.model.AssignmentStatus;
import com.campuswork.equipmentservice.repository.EquipmentAssignmentRepository;
import com.campuswork.equipmentservice.repository.EquipmentRepository;
import com.campuswork.equipmentservice.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentAssignmentRepository assignmentRepository;
    private final EquipmentMapper mapper;

    @Override
    public EquipmentResponse addEquipment(CreateEquipmentRequest dto) {
        Equipment saved = equipmentRepository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Override
    public List<EquipmentResponse> browseEquipment() {
        return equipmentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentResponse assign(AssignEquipmentRequest dto, Long assignerId) {
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new IllegalStateException("Equipment not found"));

        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new IllegalStateException("Equipment is not available for assignment");
        }

        equipment.setStatus(EquipmentStatus.ASSIGNED);
        equipmentRepository.save(equipment);

        EquipmentAssignment assignment = EquipmentAssignment.builder()
                .equipmentId(dto.getEquipmentId())
                .studentId(dto.getStudentId())
                .assignedBy(assignerId)
                .build();

        EquipmentAssignment saved = assignmentRepository.save(assignment);
        return mapper.toResponse(saved);
    }

    @Override
    public AssignmentResponse returnEquipment(Long assignmentId) {
        EquipmentAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalStateException("Assignment not found"));

        if (assignment.getStatus() == AssignmentStatus.RETURNED) {
            throw new IllegalStateException("Equipment already returned");
        }

        assignment.setStatus(AssignmentStatus.RETURNED);
        assignment.setReturnedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        Equipment equipment = equipmentRepository.findById(assignment.getEquipmentId())
                .orElseThrow(() -> new IllegalStateException("Equipment not found"));
        equipment.setStatus(EquipmentStatus.AVAILABLE);
        equipmentRepository.save(equipment);

        return mapper.toResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getMyAssignments(Long studentId) {
        return assignmentRepository.findByStudentId(studentId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}