package com.campuswork.userservice.dto;

import com.campuswork.userservice.model.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
}
