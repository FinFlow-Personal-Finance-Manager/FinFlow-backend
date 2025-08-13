package com.MohammadMarediya.FinFlow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.MohammadMarediya.FinFlow.dto.Income.IncomeRequestDto;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeResponseDto;
import com.MohammadMarediya.FinFlow.entity.Income;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.repository.IncomeRepository;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.service.interfaces.MonthlyAvailableBalanceService;
import com.MohammadMarediya.FinFlow.service.impl.IncomeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
 class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MonthlyAvailableBalanceService monthlyAvailableBalanceService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IncomeServiceImpl incomeService;

    private IncomeRequestDto incomeRequestDto;
    private User user;
    private Income income;
    private Income savedIncome;

    private final Long userId = 1L;

    @BeforeEach
    void setup() {
        incomeRequestDto = new IncomeRequestDto();
        incomeRequestDto.setAmount(new BigDecimal("1000.00"));
        incomeRequestDto.setMonth("January");
        incomeRequestDto.setYear(2023);
        incomeRequestDto.setCategory("Salary");
        incomeRequestDto.setDescription("Monthly Salary");

        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        income = new Income();
        income.setAmount(incomeRequestDto.getAmount());
        income.setMonth(incomeRequestDto.getMonth());
        income.setYear(incomeRequestDto.getYear());

        savedIncome = new Income();
        savedIncome.setId(10L);
        savedIncome.setAmount(incomeRequestDto.getAmount());
        savedIncome.setMonth(incomeRequestDto.getMonth());
        savedIncome.setYear(incomeRequestDto.getYear());
        savedIncome.setUser(user);
    }

    @Test
     void testAddIncome_Success() {

        when(modelMapper.map(incomeRequestDto, Income.class)).thenReturn(income);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(monthlyAvailableBalanceService.addMonthlyAvailableBalance(income.getMonth(), income.getYear(), income.getAmount(), user))
                .thenReturn(true);
        when(incomeRepository.save(income)).thenReturn(savedIncome);
        when(modelMapper.map(savedIncome, IncomeResponseDto.class)).thenReturn(new IncomeResponseDto());

        IncomeResponseDto response = incomeService.addIncome(incomeRequestDto, userId);

        assertNotNull(response, "Response should not be null");
        verify(modelMapper).map(incomeRequestDto, Income.class);
        verify(userRepository).findById(userId);
        verify(monthlyAvailableBalanceService).addMonthlyAvailableBalance(income.getMonth(), income.getYear(), income.getAmount(), user);
        verify(incomeRepository).save(income);
        verify(modelMapper).map(savedIncome, IncomeResponseDto.class);
    }

    @Test
     void testAddIncome_UserNotFound() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> incomeService.addIncome(incomeRequestDto, userId));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(userId);

    }

    @Test
     void testAddIncome_FailedToUpdateMonthlyBalance() {

        when(modelMapper.map(incomeRequestDto, Income.class)).thenReturn(income);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(monthlyAvailableBalanceService.addMonthlyAvailableBalance(income.getMonth(), income.getYear(), income.getAmount(), user))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> incomeService.addIncome(incomeRequestDto, userId));
        assertTrue(exception.getMessage().contains("Failed to update monthly available balance"));

        verify(modelMapper).map(incomeRequestDto, Income.class);
        verify(userRepository).findById(userId);
        verify(monthlyAvailableBalanceService).addMonthlyAvailableBalance(income.getMonth(), income.getYear(), income.getAmount(), user);
        verifyNoMoreInteractions(incomeRepository, modelMapper);
    }
}
