package com.campuswork.applicationservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyRequest {

    @NotNull
    private Long jobId;
}