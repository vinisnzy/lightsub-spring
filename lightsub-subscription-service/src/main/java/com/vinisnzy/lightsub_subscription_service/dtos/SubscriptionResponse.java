package com.vinisnzy.lightsub_subscription_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.vinisnzy.lightsub_subscription_service.model.Subscription;
import com.vinisnzy.lightsub_subscription_service.model.dtos.BillingPeriod;

public record SubscriptionResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal price,
        BillingPeriod billingPeriod,
        LocalDate renewalDate,
        String category) {
    public static SubscriptionResponse fromEntity(Subscription entity) {
        return new SubscriptionResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getPrice(),
                entity.getBillingPeriod(),
                entity.getRenewalDate(),
                entity.getCategory());
    }
}
