package com.vinisnzy.lightsub_subscription_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vinisnzy.lightsub_subscription_service.model.dtos.BillingPeriod;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscriptionRequest(

    @NotBlank(message = "User ID cannot be blank")
    String userId,

    @NotBlank(message = "Name cannot be blank")
    String name,

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    BigDecimal price,

    @NotNull(message = "Billing period cannot be null")
    BillingPeriod billingPeriod,

    @NotNull(message = "Renewal date cannot be null")
    @Future(message = "Renewal date must be in the future")
    LocalDate renewalDate,

    @NotBlank(message = "Category cannot be blank")
    String category
) {
}
