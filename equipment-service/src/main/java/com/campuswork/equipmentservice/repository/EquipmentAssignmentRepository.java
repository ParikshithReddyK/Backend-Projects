package com.campuswork.equipmentservice.repository;

import com.campuswork.equipmentservice.model.EquipmentAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentAssignmentRepository extends JpaRepository<EquipmentAssignment, Long> {
    List<EquipmentAssignment> findByStudentId(Long studentId);
}