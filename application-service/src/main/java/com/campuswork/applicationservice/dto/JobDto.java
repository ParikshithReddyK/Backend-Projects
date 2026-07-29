package com.campuswork.applicationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobDto {
    private Long id;
    private String title;
    private String status;
    private Long postedBy;
}