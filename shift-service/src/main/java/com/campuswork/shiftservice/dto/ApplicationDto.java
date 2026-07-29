package com.campuswork.shiftservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDto {
    private Long id;
    private Long jobId;
    private Long studentId;
    private String status;
}