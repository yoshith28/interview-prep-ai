package com.examly.springapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.examly.springapp.dto.LoginRequestDto;
// import com.examly.springapp.model.AuthRequest;
import com.examly.springapp.model.User;
import com.examly.springapp.dto.UserResponseDto;
import com.examly.springapp.security.JwtUtil;
import com.examly.springapp.dto.LoginResponseDto;
import com.examly.springapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody User request) {
        logger.info("Register endpoint called for email: {}", request.getEmail());
        try {
            User user = userService.registerUser(request.getUsername(), request.getPassword(), request.getEmail());
            UserResponseDto response = new UserResponseDto(user.getId(), user.getUsername(), user.getRole());
            logger.info("User registered successfully: {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration failed for email: {} - {}", request.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        logger.info("Login endpoint called for email: {}", request.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(request.getEmail());
            LoginResponseDto response = new LoginResponseDto(request.getEmail(), token);
            logger.info("Login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login failed for email: {} - {}", request.getEmail(), e.getMessage(), e);
            throw e;
        }
    }
}
