package com.vinisnzy.lightsub_billing_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vinisnzy.lightsub_billing_service.client.SubscriptionsClient;
import com.vinisnzy.lightsub_billing_service.client.dto.SubscriptionResponse;
import com.vinisnzy.lightsub_billing_service.dto.ExpensesResponse;
import com.vinisnzy.lightsub_billing_service.enums.BillingPeriod;

@ExtendWith(MockitoExtension.class)
class BillingServiceTest {
    
    @Mock
    private SubscriptionsClient subscriptionsClient;

    @InjectMocks
    private BillingService billingService;

    @Test
    @DisplayName("Should calculate monthly expenses correctly without category filter")
    void shouldCalculateMonthlyExpensesCorrectlyWithoutCategoryFilter() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Optional<String> category = Optional.empty();
        List<SubscriptionResponse> subscriptions = List.of(
            new SubscriptionResponse(UUID.randomUUID(), userId, "Netflix", new BigDecimal(15.00), BillingPeriod.MONTHLY, LocalDate.now(), "Entertainment"),
            new SubscriptionResponse(UUID.randomUUID(), userId, "Spotify", new BigDecimal(10.00), BillingPeriod.MONTHLY, LocalDate.now(), "Music")
        );

        when(subscriptionsClient.getAllSubscriptions(userId.toString())).thenReturn(subscriptions);

        // Act
        ExpensesResponse response = billingService.getSubscriptionsMonthlyExpensesPerCategory(userId.toString(), category);

        // Assert
        BigDecimal expectedAmount = new BigDecimal(25.00);

        assertNotNull(response);
        assertEquals(expectedAmount, response.amount());
        assertEquals(BillingPeriod.MONTHLY, response.period());
        assertEquals("ALL", response.category());
    }

    @Test
    @DisplayName("Should calculate monthly expenses correctly without category filter")
    void shouldCalculateMonthlyExpensesCorrectlyWithCategoryFilter() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Optional<String> category = Optional.of("Entertainment");
        List<SubscriptionResponse> subscriptions = List.of(
                new SubscriptionResponse(UUID.randomUUID(), userId, "Netflix", new BigDecimal(15.00),
                        BillingPeriod.MONTHLY, LocalDate.now(), "Entertainment"),
                new SubscriptionResponse(UUID.randomUUID(), userId, "Spotify", new BigDecimal(10.00),
                        BillingPeriod.MONTHLY, LocalDate.now(), "Music"));

        when(subscriptionsClient.getAllSubscriptions(userId.toString())).thenReturn(subscriptions);

        // Act
        ExpensesResponse response = billingService.getSubscriptionsMonthlyExpensesPerCategory(userId.toString(),
                category);

        // Assert
        BigDecimal expectedAmount = new BigDecimal(15.00);

        assertNotNull(response);
        assertEquals(expectedAmount, response.amount());
        assertEquals(BillingPeriod.MONTHLY, response.period());
        assertEquals("Entertainment", response.category());
    }

    @Test
    @DisplayName("Should calculate yearly expenses correctly without category filter")
    void shouldCalculateYearlyExpensesCorrectlyWithoutCategoryFilter() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Optional<String> category = Optional.empty();
        List<SubscriptionResponse> subscriptions = List.of(
                new SubscriptionResponse(UUID.randomUUID(), userId, "Netflix", new BigDecimal(150.00),
                        BillingPeriod.YEARLY, LocalDate.now(), "Entertainment"),
                new SubscriptionResponse(UUID.randomUUID(), userId, "Spotify", new BigDecimal(100.00),
                        BillingPeriod.YEARLY, LocalDate.now(), "Music"));

        when(subscriptionsClient.getAllSubscriptions(userId.toString())).thenReturn(subscriptions);

        // Act
        ExpensesResponse response = billingService.getSubscriptionsYearlyExpensesPerCategory(userId.toString(),
                category);

        // Assert
        BigDecimal expectedAmount = new BigDecimal(250.00);

        assertNotNull(response);
        assertEquals(expectedAmount, response.amount());
        assertEquals(BillingPeriod.YEARLY, response.period());
        assertEquals("ALL", response.category());
    }

    @Test
    @DisplayName("Should calculate yearly expenses correctly with category filter")
    void shouldCalculateYearlyExpensesCorrectlyWithCategoryFilter() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Optional<String> category = Optional.of("Entertainment");
        List<SubscriptionResponse> subscriptions = List.of(
                new SubscriptionResponse(UUID.randomUUID(), userId, "Netflix", new BigDecimal(150.00),
                        BillingPeriod.YEARLY, LocalDate.now(), "Entertainment"),
                new SubscriptionResponse(UUID.randomUUID(), userId, "Spotify", new BigDecimal(100.00),
                        BillingPeriod.YEARLY, LocalDate.now(), "Music"));

        when(subscriptionsClient.getAllSubscriptions(userId.toString())).thenReturn(subscriptions);

        // Act
        ExpensesResponse response = billingService.getSubscriptionsYearlyExpensesPerCategory(userId.toString(),
                category);

        // Assert
        BigDecimal expectedAmount = new BigDecimal(150.00);

        assertNotNull(response);
        assertEquals(expectedAmount, response.amount());
        assertEquals(BillingPeriod.YEARLY, response.period());
        assertEquals("Entertainment", response.category());
    }
}
