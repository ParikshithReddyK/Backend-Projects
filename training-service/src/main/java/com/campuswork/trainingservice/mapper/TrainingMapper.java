package com.campuswork.trainingservice.mapper;

import com.campuswork.trainingservice.dto.*;
import com.campuswork.trainingservice.model.TrainingCompletion;
import com.campuswork.trainingservice.model.TrainingModule;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public TrainingModule toEntity(CreateModuleRequest dto, Long createdBy) {
        return TrainingModule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .createdBy(createdBy)
                .build();
    }

    public ModuleResponse toResponse(TrainingModule entity) {
        return ModuleResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public CompletionResponse toResponse(TrainingCompletion entity) {
        return CompletionResponse.builder()
                .id(entity.getId())
                .moduleId(entity.getModuleId())
                .studentId(entity.getStudentId())
                .status(entity.getStatus())
                .score(entity.getScore())
                .enrolledAt(entity.getEnrolledAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }
}