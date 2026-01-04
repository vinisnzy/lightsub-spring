package com.vinisnzy.lightsub_subscription_service.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.vinisnzy.lightsub_subscription_service.model.Subscription;
import com.vinisnzy.lightsub_subscription_service.model.dtos.BillingPeriod;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscriptionRequest(

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
    public static Subscription toEntity(UUID userId, SubscriptionRequest request) {
        var subscription = new Subscription(); 
        subscription.setUserId(userId);
        subscription.setName(request.name());
        subscription.setPrice(request.price());
        subscription.setBillingPeriod(request.billingPeriod());
        subscription.setRenewalDate(request.renewalDate());
        subscription.setCategory(request.category());
        return subscription;
    }
}
