package com.campuswork.userservice.service;

import com.campuswork.userservice.dto.LoginRequest;
import com.campuswork.userservice.dto.RegisterRequest;
import com.campuswork.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(RegisterRequest request);
    String login(LoginRequest request);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
}