package com.MohammadMarediya.FinFlow.repository;

import com.MohammadMarediya.FinFlow.entity.MonthlyAvailableBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonthlyAvailableBalanceRepository extends JpaRepository<MonthlyAvailableBalance, Long> {

    @Query(value = "SELECT * FROM available_balance WHERE MONTH = :month AND YEAR = :year AND USER_iD = :userId", nativeQuery = true)
    MonthlyAvailableBalance findByMonthAndYearAndUserId(@Param("month") String month, @Param("year") Integer year, @Param("userId") Long userId);

    //boolean existsByMonthAndYearAndUserId(String month, Integer year, Long userId);
}