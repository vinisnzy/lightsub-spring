package com.vinisnzy.lightsub_subscription_service.integration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionRequest;
import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionResponse;
import com.vinisnzy.lightsub_subscription_service.model.Subscription;
import com.vinisnzy.lightsub_subscription_service.model.enums.BillingPeriod;
import com.vinisnzy.lightsub_subscription_service.repository.SubscriptionRepository;
import com.vinisnzy.lightsub_subscription_service.service.SubscriptionService;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SubscriptionServiceIT {
    
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    @DisplayName("Should get all subscriptions for a user")
    void testGetAllSubscriptions() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Act
        List<SubscriptionResponse> response = subscriptionService.getAllSubscriptions(userId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("Should get subscription by ID")
    void testGetSubscriptionById() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setUserId(UUID.randomUUID());
        subscription.setName("Netflix");
        subscription.setPrice(new BigDecimal(15.99));
        subscription.setBillingPeriod(BillingPeriod.MONTHLY);
        subscription.setRenewalDate(LocalDate.now().plusMonths(1));
        subscription.setCategory("Entertainment");

        subscription = subscriptionRepository.save(subscription);

        UUID subscriptionId = subscription.getId();

        // Act
        SubscriptionResponse response = subscriptionService.getSubscriptionById(subscriptionId);

        // Assert
        assertNotNull(response);
        assertEquals(subscriptionId, response.id());
    }

    @Test
    @DisplayName("Should create SubscriptionService instance successfully")
    void testCreateSubscription() {
        // Arrange
        SubscriptionRequest request = new SubscriptionRequest("Netflix", new BigDecimal(15.99), BillingPeriod.MONTHLY, LocalDate.now().plusMonths(1), "Entertainment");

        UUID userId = UUID.randomUUID();

        // Act
        SubscriptionResponse response = subscriptionService.createSubscription(userId, request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(userId, response.userId());
    }

    @Test
    @DisplayName("Should update Subscription successfully")
    void testUpdateSubscription() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setUserId(UUID.randomUUID());
        subscription.setName("Spotify");
        subscription.setPrice(new BigDecimal(10.99));
        subscription.setBillingPeriod(BillingPeriod.MONTHLY);
        subscription.setRenewalDate(LocalDate.now().plusMonths(1));
        subscription.setCategory("Music");

        subscription = subscriptionRepository.save(subscription);

        SubscriptionRequest request = new SubscriptionRequest("Netflix", new BigDecimal(15.99), BillingPeriod.MONTHLY,
        LocalDate.now().plusMonths(1), "Entertainment");

        // Act
        SubscriptionResponse response = subscriptionService.updateSubscription(subscription.getId(), request);

        // Assert
        assertNotNull(response);
        assertEquals(subscription.getId(), response.id());
        assertEquals(request.name(), response.name());
        assertEquals(request.price(), response.price());
        assertEquals(request.billingPeriod(), response.billingPeriod());
        assertEquals(request.renewalDate(), response.renewalDate());
        assertEquals(request.category(), response.category());
    }
}
