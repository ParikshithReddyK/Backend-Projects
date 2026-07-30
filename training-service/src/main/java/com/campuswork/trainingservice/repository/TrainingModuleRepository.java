package com.campuswork.trainingservice.repository;

import com.campuswork.trainingservice.model.TrainingModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingModuleRepository extends JpaRepository<TrainingModule, Long> {
}