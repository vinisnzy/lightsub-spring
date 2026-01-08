package com.vinisnzy.lightsub_billing_service.dto;

import java.math.BigDecimal;

import com.vinisnzy.lightsub_billing_service.enums.BillingPeriod;

public record ExpensesResponse(
    String category,
    BillingPeriod period,
    BigDecimal amount
) {
}
