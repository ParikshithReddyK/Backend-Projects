package com.campuswork.shiftservice.repository;

import com.campuswork.shiftservice.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByStudentId(Long studentId);
    List<Shift> findByJobId(Long jobId);
}
