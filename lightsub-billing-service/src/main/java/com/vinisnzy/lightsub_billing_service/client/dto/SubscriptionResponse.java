package com.vinisnzy.lightsub_billing_service.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.vinisnzy.lightsub_billing_service.enums.BillingPeriod;

public record SubscriptionResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal price,
        BillingPeriod billingPeriod,
        LocalDate renewalDate,
        String category
) {
}
