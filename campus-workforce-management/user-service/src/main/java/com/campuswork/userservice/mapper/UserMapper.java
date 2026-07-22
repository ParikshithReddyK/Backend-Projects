package com.campuswork.userservice.mapper;

import com.campuswork.userservice.dto.RegisterRequest;
import com.campuswork.userservice.dto.UserResponse;
import com.campuswork.userservice.model.Role;
import com.campuswork.userservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
