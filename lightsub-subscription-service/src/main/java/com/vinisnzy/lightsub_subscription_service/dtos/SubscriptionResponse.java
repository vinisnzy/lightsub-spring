package com.vinisnzy.lightsub_subscription_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vinisnzy.lightsub_subscription_service.model.dtos.BillingPeriod;

public record SubscriptionResponse(
    String id,
    String userId,
    String name,
    BigDecimal price,
    BillingPeriod billingPeriod,
    LocalDate renewalDate,
    String category) {
}
