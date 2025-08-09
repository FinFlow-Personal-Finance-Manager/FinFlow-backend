package com.MohammadMarediya.FinFlow.dto.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email or mobile number is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
