package com.MohammadMarediya.FinFlow.service;

import com.MohammadMarediya.FinFlow.entity.MonthlyAvailableBalance;
import com.MohammadMarediya.FinFlow.entity.User;
import com.MohammadMarediya.FinFlow.repository.MonthlyAvailableBalanceRepository;
import com.MohammadMarediya.FinFlow.repository.UserRepository;
import com.MohammadMarediya.FinFlow.service.impl.MonthlyAvailableBalanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class MonthlyAvailableBalanceServiceImplTest {

    @Mock
    private MonthlyAvailableBalanceRepository repository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MonthlyAvailableBalanceServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
    }

    @Test
     void nullUserTest() {
        boolean result = service.addMonthlyAvailableBalance("August", 2025, new BigDecimal(1234), null);
        assertFalse(result);
    }

    @Test
     void nullMonthTest() {
        boolean result = service.addMonthlyAvailableBalance(null, 2025, new BigDecimal(1234), user);
        assertFalse(result);
    }

    @Test
     void monthEmptyTest() {
        boolean result = service.addMonthlyAvailableBalance("  ", 2025, new BigDecimal(1234), user);
        assertFalse(result);
    }

    @Test
     void nullYearTest() {
        boolean result = service.addMonthlyAvailableBalance("August", null, new BigDecimal(1234), user);
        assertFalse(result);
    }

    @Test
     void amountNegativeTest() {
        boolean result = service.addMonthlyAvailableBalance("August", 2025, new BigDecimal(0), user);
        assertFalse(result);
    }

    @Test
     void UpdateExistingBalanceTest() {

        MonthlyAvailableBalance existing = new MonthlyAvailableBalance();
        existing.setMonth("January");
        existing.setYear(2025);
        existing.setBalance(BigDecimal.valueOf(500));
        existing.setUser(user);

        when(repository.findByMonthAndYearAndUserId("January", 2025, user.getId()))
                .thenReturn(existing);

        boolean result = service.addMonthlyAvailableBalance("January", 2025, BigDecimal.valueOf(1000), user);

        assertTrue(result);
        assertEquals(BigDecimal.valueOf(1500), existing.getBalance());
        verify(repository).save(existing);
    }

    @Test
     void addNewBalanceTest() {

        when(repository.findByMonthAndYearAndUserId("January", 2023, user.getId()))
                .thenReturn(null);

        boolean result = service.addMonthlyAvailableBalance("January", 2023, BigDecimal.valueOf(1000), user);

        assertTrue(result);

    }


}
