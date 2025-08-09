package com.MohammadMarediya.FinFlow.controller;

import com.MohammadMarediya.FinFlow.Constant.ApiEndpoint;
import com.MohammadMarediya.FinFlow.dto.auth.AuthResponseDTO;
import com.MohammadMarediya.FinFlow.dto.auth.LoginRequestDTO;
import com.MohammadMarediya.FinFlow.dto.auth.RegisterRequestDTO;
import com.MohammadMarediya.FinFlow.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoint.AUTH_ENDPOINT)
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiEndpoint.REGISTER_ENDPOINT)
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        AuthResponseDTO response = authService.registerUser(request);

        log.info("User registered successfully with email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping(ApiEndpoint.LOGIN_ENDPOINT)
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("User login attempt with email: {}", request.getEmail());

        AuthResponseDTO response = authService.loginUser(request);

        log.info("User logged in successfully: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
