package com.MohammadMarediya.FinFlow.service.interfaces;

import com.MohammadMarediya.FinFlow.dto.MonthLyAvailableBalance.MonthlyAvailableBalanceResponseDTO;
import com.MohammadMarediya.FinFlow.entity.User;

import java.math.BigDecimal;

public interface MonthlyAvailableBalanceService {

    boolean addMonthlyAvailableBalance(String month, Integer year, BigDecimal amount, User user);

    MonthlyAvailableBalanceResponseDTO getMonthlyAvailableBalance(String month, Integer year, Long userId);
}
