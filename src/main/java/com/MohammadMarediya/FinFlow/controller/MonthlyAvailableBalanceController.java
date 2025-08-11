package com.MohammadMarediya.FinFlow.controller;

import com.MohammadMarediya.FinFlow.Constant.ApiEndpoint;
import com.MohammadMarediya.FinFlow.dto.MonthLyAvailableBalance.MonthlyAvailableBalanceResponseDTO;
import com.MohammadMarediya.FinFlow.security.JwtUtil;
import com.MohammadMarediya.FinFlow.service.interfaces.MonthlyAvailableBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;

@RestController
@RequestMapping(ApiEndpoint.MONTHLY_AVAILABLE_BALANCE_ENDPOINT)
@Slf4j
@RequiredArgsConstructor
public class MonthlyAvailableBalanceController {

    private final MonthlyAvailableBalanceService monthlyAvailableBalanceService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{month}/{year}")
    public ResponseEntity<MonthlyAvailableBalanceResponseDTO> getMonthlyAvailableBalance(@PathVariable String month, @PathVariable Integer year) {
        log.info("Fetching monthly available balance for month: {}, year: {}", month, year);
        Long currentLoginUserId = jwtUtil.getCurrentUserId();
        MonthlyAvailableBalanceResponseDTO response = monthlyAvailableBalanceService.getMonthlyAvailableBalance(month, year, currentLoginUserId);
        log.info("Monthly available balance fetched successfully for month: {}, year: {}", month, year);
        return ResponseEntity.ok(response);
    }

}
