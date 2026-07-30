package com.campuswork.trainingservice.service;

import com.campuswork.trainingservice.dto.*;

import java.util.List;

public interface TrainingService {
    ModuleResponse createModule(CreateModuleRequest dto, Long creatorId);
    List<ModuleResponse> browseModules();
    CompletionResponse enroll(Long moduleId, Long studentId);
    CompletionResponse completeTraining(Long completionId, CompleteTrainingRequest dto, Long studentId);
    List<CompletionResponse> getMyCompletions(Long studentId);
}