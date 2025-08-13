package com.MohammadMarediya.FinFlow.controller;

import com.MohammadMarediya.FinFlow.Constant.ApiEndpoint;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeRequestDto;
import com.MohammadMarediya.FinFlow.dto.Income.IncomeResponseDto;
import com.MohammadMarediya.FinFlow.security.JwtUtil;
import com.MohammadMarediya.FinFlow.service.interfaces.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ApiEndpoint.INCOME_ENDPOINT)
public class IncomeController {

    private final IncomeService incomeService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<IncomeResponseDto> addIncome(@Valid @RequestBody IncomeRequestDto incomeRequestDto) {
        log.info("Received income request: {}", incomeRequestDto);
        Long currentLoginUserId = jwtUtil.getCurrentUserId();
        IncomeResponseDto response = incomeService.addIncome(incomeRequestDto, currentLoginUserId);
        log.info("Income added successfully for user ID: {}", currentLoginUserId);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{incomeId}")
    public ResponseEntity<List<IncomeResponseDto>> getIncomeById(@PathVariable Long incomeId) {

        log.info("Fetching income by ID");
        Long currentLoginUserId = jwtUtil.getCurrentUserId();

        List<IncomeResponseDto> response = incomeService.getIncomeById(incomeId, currentLoginUserId);
        log.info("Income fetched successfully for user ID: {}", currentLoginUserId);

        return ResponseEntity.ok(response);
    }


}
