package com.MohammadMarediya.FinFlow.repository;

import com.MohammadMarediya.FinFlow.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<List<Income>> findByUserId(Long userId);

}