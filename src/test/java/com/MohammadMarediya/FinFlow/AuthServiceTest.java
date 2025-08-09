package com.MohammadMarediya.FinFlow;

import com.MohammadMarediya.FinFlow.dto.auth.AuthResponseDTO;
import com.MohammadMarediya.FinFlow.dto.auth.LoginRequestDTO;
import com.MohammadMarediya.FinFlow.dto.auth.RegisterRequestDTO;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.enums.Role;
import com.MohammadMarediya.FinFlow.exception.UserAlreadyExistsException;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.security.JwtUtil;
import com.MohammadMarediya.FinFlow.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    private RegisterRequestDTO requestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new RegisterRequestDTO();
        requestDTO.setName("Mohammad");
        requestDTO.setEmail("mohammad@example.com");
        requestDTO.setMobileNumber("9624986226");
        requestDTO.setPassword("password");

        user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setMobileNumber(requestDTO.getMobileNumber());
    }


    @Test
    void registerUser_success_userRole() {
        // Arrange
        when(modelMapper.map(requestDTO, User.class)).thenReturn(user);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mockToken");

        AuthResponseDTO mockResponse = new AuthResponseDTO();
        when(modelMapper.map(user, AuthResponseDTO.class)).thenReturn(mockResponse);

        // Act
        AuthResponseDTO result = authService.registerUser(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals(Role.USER, user.getRoles());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void registerUser_fail_emailAlreadyExists() {

        when(modelMapper.map(requestDTO, User.class)).thenReturn(user);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.registerUser(requestDTO)
        );

        assertTrue(exception.getMessage().contains("Email"));
        verify(userRepository, never()).save(any());
    }


    @Test
    void registerUser_success_adminRole() {

        requestDTO.setRole("ADMIN");

        when(modelMapper.map(requestDTO, User.class)).thenReturn(user);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByMobileNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mockToken");

        AuthResponseDTO mockResponse = new AuthResponseDTO();
        when(modelMapper.map(user, AuthResponseDTO.class)).thenReturn(mockResponse);


        AuthResponseDTO result = authService.registerUser(requestDTO);

        assertNotNull(result);
        assertEquals("mockToken", result.getToken());
        assertEquals(Role.ADMIN, user.getRoles());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void loginUser_Test() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class))
        ).thenReturn(auth);

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        when(auth.getPrincipal()).thenReturn(mockUser);

        when(jwtUtil.generateToken(request.getEmail(), Role.USER.name()))
                .thenReturn("fake-jwt-token");

        AuthResponseDTO response = authService.loginUser(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil, times(1))
                .generateToken(request.getEmail(), Role.USER.name());
    }

}


