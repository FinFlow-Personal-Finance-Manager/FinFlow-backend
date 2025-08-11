package com.MohammadMarediya.FinFlow.service.impl;

import com.MohammadMarediya.FinFlow.dto.MonthLyAvailableBalance.MonthlyAvailableBalanceResponseDTO;
import com.MohammadMarediya.FinFlow.entity.MonthlyAvailableBalance;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.exception.ResourceNotFoundException;
import com.MohammadMarediya.FinFlow.repository.MonthlyAvailableBalanceRepository;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.service.interfaces.MonthlyAvailableBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class MonthlyAvailableBalanceServiceImpl implements MonthlyAvailableBalanceService {

    private final MonthlyAvailableBalanceRepository repository;
    private final UserRepository userRepository;

    @Override
    public boolean addMonthlyAvailableBalance(String month, Integer year, BigDecimal amount, User user) {

        log.info("Adding monthly available balance for month: {}, year: {}, amount: {}", month, year, amount);

        if (user == null || user.getId() == null) {
            log.warn("User cannot be null or have a null");
            return false;
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Amount cannot be negative. Provided amount: {}", amount);
            return false;
        }
        if (year == null) {
            log.warn("Year cannot be null. Provided year: {}", year);
            return false;
        }
        if (month == null || month.isBlank()) {
            log.error("Month cannot be null or empty. Provided month: {}", month);
            return false;
        }

        MonthlyAvailableBalance check = repository.findByMonthAndYearAndUserId(month, year, user.getId());
        if (check != null) {
            log.info("Monthly available balance for month: {}, year: {} already exists for user ID: {}", month, year, user.getId());
            BigDecimal balance = check.getBalance();
            if (check.getYear().equals(year) && check.getMonth().equals(month)) {
                balance = balance.add(amount);
                log.info("Updating existing monthly available balance for month: {}, year: {}. Current balance: {}, New amount: {}", month, year, check.getBalance(), amount);
                check.setBalance(balance);
                repository.save(check);
            }
            log.info("Monthly available balance updated successfully for month: {}, year: {}", month, year);
            return true;
        }
        log.info("Creating new monthly available balance for month: {}, year: {}", month, year);
        MonthlyAvailableBalance balance = new MonthlyAvailableBalance();
        balance.setYear(year);
        balance.setMonth(month);
        balance.setBalance(amount);
        balance.setUser(user);

        repository.save(balance);
        log.info("Monthly available balance added successfully for month: {}, year: {}", month, year);

        return true;
    }


    @Override
    public MonthlyAvailableBalanceResponseDTO getMonthlyAvailableBalance(String month, Integer year, Long userId) {

        log.info("Retrieving monthly available balance for month: {}, year: {}, user ID: {}", month, year, userId);

        if (userId == null) {
            log.warn("User cannot be null or have a null");
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (year == null) {
            log.warn("Year cannot be null. Provided year: {}", year);
            throw new IllegalArgumentException("Year cannot be null");
        }
        if (month == null || month.isBlank()) {
            log.error("Month cannot be null or empty. Provided month: {}", month);
            throw new IllegalArgumentException("Month cannot be null or empty");
        }
        MonthlyAvailableBalance balance = repository.findByMonthAndYearAndUserId(month, year, userId);
        if (balance == null) {
            log.warn("No monthly available balance found for month: {}, year: {}, user ID: {}", month, year, userId);
            throw new ResourceNotFoundException("No monthly available balance found for the specified month and year");
        }
        MonthlyAvailableBalanceResponseDTO response = new MonthlyAvailableBalanceResponseDTO();
        response.setMonth(balance.getMonth());
        response.setYear(balance.getYear());
        response.setAvailableBalance(balance.getBalance());
        log.info("Monthly available balance retrieved successfully for month: {}, year: {}, user ID: {}", month, year, userId);
        return response;


    }
}
