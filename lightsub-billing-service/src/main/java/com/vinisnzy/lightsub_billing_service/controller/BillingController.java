package com.vinisnzy.lightsub_billing_service.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vinisnzy.lightsub_billing_service.dto.ExpensesResponse;
import com.vinisnzy.lightsub_billing_service.service.BillingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {
    
    private final BillingService billingService;

    @GetMapping("/monthly")
    public ResponseEntity<ExpensesResponse> getMonthlyExpensesPerCategory(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) String category) {
        ExpensesResponse response = billingService.getSubscriptionsMonthlyExpensesPerCategory(userId, Optional.ofNullable(category));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly")
    public ResponseEntity<ExpensesResponse> getYearlyExpensesPerCategory(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(required = false) String category) {
        ExpensesResponse response = billingService.getSubscriptionsYearlyExpensesPerCategory(userId, Optional.ofNullable(category));
        return ResponseEntity.ok(response);
    }
}
