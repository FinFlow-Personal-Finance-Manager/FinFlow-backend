package com.MohammadMarediya.FinFlow.service.interfaces;

import com.MohammadMarediya.FinFlow.dto.auth.AuthResponseDTO;
import com.MohammadMarediya.FinFlow.dto.auth.LoginRequestDTO;
import com.MohammadMarediya.FinFlow.dto.auth.RegisterRequestDTO;

public interface AuthService {

    public AuthResponseDTO registerUser(RegisterRequestDTO registerRequestDTO);

    public AuthResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
}
