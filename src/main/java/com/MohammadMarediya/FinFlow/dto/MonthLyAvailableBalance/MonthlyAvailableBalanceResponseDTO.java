package com.MohammadMarediya.FinFlow.dto.MonthLyAvailableBalance;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyAvailableBalanceResponseDTO {

    private String month;
    private Integer year;
    private BigDecimal availableBalance;


}
