package com.MohammadMarediya.FinFlow.service.impl;

import com.MohammadMarediya.FinFlow.dto.Income.IncomeRequestDto;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeResponseDto;
import com.MohammadMarediya.FinFlow.entity.Income;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.repository.IncomeRepository;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.service.interfaces.IncomeService;
import com.MohammadMarediya.FinFlow.service.interfaces.MonthlyAvailableBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final ModelMapper modelMapper;
    private final MonthlyAvailableBalanceService monthlyAvailableBalanceService;
    private final UserRepository userRepository;

    @Override
    public IncomeResponseDto addIncome(IncomeRequestDto incomeRequestDto, Long userId) {
        log.info("Adding income for user ID: {}", userId);
        Income income = modelMapper.map(incomeRequestDto, Income.class);
        log.info("Mapped Income entity: {}", income);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        boolean isSaved = monthlyAvailableBalanceService.addMonthlyAvailableBalance(income.getMonth(), income.getYear(), income.getAmount(), user);
        if (isSaved) {
            income.setUser(user);
            Income savedIncome = incomeRepository.save(income);
            log.info("Income saved successfully: {}", savedIncome);
            IncomeResponseDto incomeResponseDto = modelMapper.map(savedIncome, IncomeResponseDto.class);
            log.info("Income added successfully: {}", incomeResponseDto);
            return incomeResponseDto;
        }
        log.warn("Failed to update monthly available balance for user ID: {}", userId);
        throw new RuntimeException("Failed to update monthly available balance for user ID: " + userId);

    }
}
