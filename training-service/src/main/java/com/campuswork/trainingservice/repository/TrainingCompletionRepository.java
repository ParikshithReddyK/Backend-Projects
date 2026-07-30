package com.campuswork.trainingservice.repository;

import com.campuswork.trainingservice.model.TrainingCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingCompletionRepository extends JpaRepository<TrainingCompletion, Long> {
    List<TrainingCompletion> findByStudentId(Long studentId);
    Optional<TrainingCompletion> findByModuleIdAndStudentId(Long moduleId, Long studentId);
}