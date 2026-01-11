package com.vinisnzy.lightsub_billing_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vinisnzy.lightsub_billing_service.client.SubscriptionsClient;
import com.vinisnzy.lightsub_billing_service.client.dto.SubscriptionResponse;
import com.vinisnzy.lightsub_billing_service.dto.ExpensesResponse;
import com.vinisnzy.lightsub_billing_service.enums.BillingPeriod;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final SubscriptionsClient client;

    public ExpensesResponse getSubscriptionsMonthlyExpensesPerCategory(String userId, Optional<String> category) {
        List<SubscriptionResponse> subscriptions = client.getAllSubscriptions(userId);

        BigDecimal amount = getAmountByCategoryAndPeriod(subscriptions, category, BillingPeriod.MONTHLY);

        return new ExpensesResponse(category.orElse("ALL"), BillingPeriod.MONTHLY, amount);
    }

    public ExpensesResponse getSubscriptionsYearlyExpensesPerCategory(String userId, Optional<String> category) {
        List<SubscriptionResponse> subscriptions = client.getAllSubscriptions(userId);

        BigDecimal amount = getAmountByCategoryAndPeriod(subscriptions, category, BillingPeriod.YEARLY);

        return new ExpensesResponse(category.orElse("ALL"), BillingPeriod.YEARLY, amount);
    }

    private BigDecimal getAmountByCategoryAndPeriod(List<SubscriptionResponse> subscriptions, Optional<String> category,
            BillingPeriod period) {
        return subscriptions.stream()
                .filter(s -> period.equals(s.billingPeriod()))
                .filter(s -> category
                        .map(c -> s.category().equalsIgnoreCase(c))
                        .orElse(true))
                .map(SubscriptionResponse::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
