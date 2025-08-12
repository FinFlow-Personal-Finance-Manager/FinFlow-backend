package com.MohammadMarediya.FinFlow.service.impl;

import com.MohammadMarediya.FinFlow.dto.auth.AuthResponseDTO;
import com.MohammadMarediya.FinFlow.dto.auth.LoginRequestDTO;
import com.MohammadMarediya.FinFlow.dto.auth.RegisterRequestDTO;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.enums.ErrorCodeMessage;
import com.MohammadMarediya.FinFlow.enums.Role;
import com.MohammadMarediya.FinFlow.exception.UserAlreadyExistsException;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.security.JwtUtil;
import com.MohammadMarediya.FinFlow.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        log.info("Attempting to register user with request: {}", registerRequestDTO);

        User user = modelMapper.map(registerRequestDTO, User.class);

        log.info("Mapped User entity: {}", user);

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Registration failed: Email '{}' already exists", user.getEmail());
            throw new UserAlreadyExistsException(ErrorCodeMessage.EMAIL_ALREADY_EXIST.getErrorCode(), ErrorCodeMessage.EMAIL_ALREADY_EXIST.getMessage());
        }

        if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
            log.warn("Registration failed: Mobile number '{}' already exists", user.getMobileNumber());
            throw new UserAlreadyExistsException(ErrorCodeMessage.MOBILE_NUMBER_ALREADY_EXIST.getErrorCode(), ErrorCodeMessage.MOBILE_NUMBER_ALREADY_EXIST.getMessage());
        }

        if (registerRequestDTO.getRole() != null && registerRequestDTO.getRole().equals("ADMIN")) {
            log.info("Admin role requested. Assigning ADMIN role to '{}'", registerRequestDTO.getEmail());
            user.setRoles(Role.ADMIN);
        } else {
            log.info("No admin role requested. Assigning default USER role to '{}'", registerRequestDTO.getEmail());
            user.setRoles(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRoles().name());
        AuthResponseDTO response = modelMapper.map(savedUser, AuthResponseDTO.class);
        response.setToken(token);

        log.info("User registered successfully for email: {}", savedUser.getEmail());
        log.info("User registered successfully: {}", response);
        return response;

    }

    @Override
    public AuthResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {

        log.info("Attempting to Login user with request: {}", loginRequestDTO.getEmail() );

        // 1. Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));

        // 2. Generate JWT Token
        String token = jwtUtil.generateToken(
                loginRequestDTO.getEmail(),
                user.getRoles().name()
        );

        log.info("Authentication successful for: {}", loginRequestDTO.getEmail());

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        return response;
    }
}
