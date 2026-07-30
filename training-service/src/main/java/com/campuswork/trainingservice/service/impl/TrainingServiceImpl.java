package com.campuswork.trainingservice.service.impl;

import com.campuswork.trainingservice.dto.*;
import com.campuswork.trainingservice.mapper.TrainingMapper;
import com.campuswork.trainingservice.model.CompletionStatus;
import com.campuswork.trainingservice.model.TrainingCompletion;
import com.campuswork.trainingservice.model.TrainingModule;
import com.campuswork.trainingservice.repository.TrainingCompletionRepository;
import com.campuswork.trainingservice.repository.TrainingModuleRepository;
import com.campuswork.trainingservice.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingModuleRepository moduleRepository;
    private final TrainingCompletionRepository completionRepository;
    private final TrainingMapper mapper;

    @Override
    public ModuleResponse createModule(CreateModuleRequest dto, Long creatorId) {
        TrainingModule saved = moduleRepository.save(mapper.toEntity(dto, creatorId));
        return mapper.toResponse(saved);
    }

    @Override
    public List<ModuleResponse> browseModules() {
        return moduleRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CompletionResponse enroll(Long moduleId, Long studentId) {
        moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalStateException("Training module not found"));

        if (completionRepository.findByModuleIdAndStudentId(moduleId, studentId).isPresent()) {
            throw new IllegalStateException("Already enrolled in this module");
        }

        TrainingCompletion completion = TrainingCompletion.builder()
                .moduleId(moduleId)
                .studentId(studentId)
                .build();

        TrainingCompletion saved = completionRepository.save(completion);
        return mapper.toResponse(saved);
    }

    @Override
    public CompletionResponse completeTraining(Long completionId, CompleteTrainingRequest dto, Long studentId) {
        TrainingCompletion completion = completionRepository.findById(completionId)
                .orElseThrow(() -> new IllegalStateException("Enrollment not found"));

        if (!completion.getStudentId().equals(studentId)) {
            throw new AccessDeniedException("You can only complete your own training");
        }

        completion.setStatus(CompletionStatus.COMPLETED);
        completion.setScore(dto.getScore());
        completion.setCompletedAt(LocalDateTime.now());

        TrainingCompletion updated = completionRepository.save(completion);
        return mapper.toResponse(updated);
    }

    @Override
    public List<CompletionResponse> getMyCompletions(Long studentId) {
        return completionRepository.findByStudentId(studentId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}