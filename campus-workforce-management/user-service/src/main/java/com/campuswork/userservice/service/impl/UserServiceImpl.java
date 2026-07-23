package com.campuswork.userservice.service.impl;

import com.campuswork.userservice.dto.LoginRequest;
import com.campuswork.userservice.dto.RegisterRequest;
import com.campuswork.userservice.dto.UserResponse;
import com.campuswork.userservice.mapper.UserMapper;
import com.campuswork.userservice.model.User;
import com.campuswork.userservice.repository.UserRepository;
import com.campuswork.userservice.security.JwtUtil;
import com.campuswork.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already registered: " + request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toEntity(request, encodedPassword);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}