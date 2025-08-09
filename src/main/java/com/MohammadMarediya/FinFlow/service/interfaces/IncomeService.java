package com.MohammadMarediya.FinFlow.service.interfaces;

import com.MohammadMarediya.FinFlow.dto.Income.IncomeRequestDto;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeResponseDto;

public interface IncomeService {

    IncomeResponseDto addIncome(IncomeRequestDto IncomeRequestDto, Long userId);

}
