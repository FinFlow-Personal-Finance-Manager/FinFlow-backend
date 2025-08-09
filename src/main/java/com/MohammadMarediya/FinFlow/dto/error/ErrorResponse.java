package com.MohammadMarediya.FinFlow.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private int errorCode;
    private String message;
    private String path;
}
