package com.vinisnzy.lightsub_billing_service.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID id,
        UUID userId,
        String name,
        BigDecimal price,
        String billingPeriod,
        LocalDate renewalDate,
        String category
) {
}
