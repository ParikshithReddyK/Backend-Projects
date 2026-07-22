package com.campuswork.userservice.service;

import com.campuswork.userservice.dto.RegisterRequest;
import com.campuswork.userservice.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(RegisterRequest request);
}
