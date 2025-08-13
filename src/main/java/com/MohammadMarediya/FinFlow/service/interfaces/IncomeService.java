package com.MohammadMarediya.FinFlow.service.interfaces;

import com.MohammadMarediya.FinFlow.dto.Income.IncomeRequestDto;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeResponseDto;

import java.util.List;

public interface IncomeService {

    IncomeResponseDto addIncome(IncomeRequestDto requestDto, Long userId);
    List<IncomeResponseDto> getIncomeById(Long incomeId, Long userId);

}
