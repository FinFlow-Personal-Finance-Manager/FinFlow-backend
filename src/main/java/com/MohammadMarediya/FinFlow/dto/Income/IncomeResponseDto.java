package com.MohammadMarediya.FinFlow.dto.Income;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class IncomeResponseDto {

    private String category;

    private BigDecimal amount;

    private String description;

    private LocalDateTime addedDate;

}
