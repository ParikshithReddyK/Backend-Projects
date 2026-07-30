package com.campuswork.trainingservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModuleResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Long createdBy;
}