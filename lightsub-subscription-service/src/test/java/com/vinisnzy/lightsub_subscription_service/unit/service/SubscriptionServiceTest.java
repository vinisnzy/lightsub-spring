package com.vinisnzy.lightsub_subscription_service.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionRequest;
import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionResponse;
import com.vinisnzy.lightsub_subscription_service.exceptions.ResourceNotFoundException;
import com.vinisnzy.lightsub_subscription_service.model.Subscription;
import com.vinisnzy.lightsub_subscription_service.model.enums.BillingPeriod;
import com.vinisnzy.lightsub_subscription_service.repository.SubscriptionRepository;
import com.vinisnzy.lightsub_subscription_service.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository repository;
    
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("Should get all subscriptions for a user")
    void testGetAllSubscriptions() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Subscription subscription1 = new Subscription();
        subscription1.setId(UUID.randomUUID());
        subscription1.setName("Netflix");
        subscription1.setPrice(new BigDecimal(15.99));
        subscription1.setBillingPeriod(BillingPeriod.MONTHLY);

        Subscription subscription2 = new Subscription();
        subscription2.setId(UUID.randomUUID());
        subscription2.setName("Spotify");
        subscription2.setPrice(new BigDecimal(9.99));
        subscription2.setBillingPeriod(BillingPeriod.MONTHLY);

        when(repository.findByUserId(userId)).thenReturn(List.of(subscription1, subscription2));

        // Act
        List<SubscriptionResponse> response = subscriptionService.getAllSubscriptions(userId);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Netflix", response.get(0).name());
        assertEquals("Spotify", response.get(1).name());
        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should get subscription by ID")
    void testGetSubscriptionById() {
        // Arrange
        UUID subscriptionId = UUID.randomUUID();
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);
        subscription.setName("Netflix");
        subscription.setPrice(new BigDecimal(15.99));
        subscription.setBillingPeriod(BillingPeriod.MONTHLY);

        when(repository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

        // Act
        SubscriptionResponse response = subscriptionService.getSubscriptionById(subscriptionId);

        // Assert
        assertNotNull(response);
        assertEquals("Netflix", response.name());
        assertEquals(new BigDecimal(15.99), response.price());
        assertEquals(BillingPeriod.MONTHLY, response.billingPeriod());
        verify(repository, times(1)).findById(subscriptionId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when subscription ID not found")
    void testGetSubscriptionById_NotFound() {
        // Arrange
        UUID subscriptionId = UUID.randomUUID();
        when(repository.findById(subscriptionId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> subscriptionService.getSubscriptionById(subscriptionId));

        // Assert
        assertNotNull(exception);
        assertEquals("Subscription not found with id: " + subscriptionId, exception.getMessage());
        verify(repository, times(1)).findById(subscriptionId);
    }

    @Test
    @DisplayName("Should create SubscriptionService instance successfully")
    void testCreateSubscriptionService() {
        // Arrange
        UUID userId = UUID.randomUUID();
        SubscriptionRequest request = new SubscriptionRequest("Netflix", new BigDecimal(15.99), BillingPeriod.MONTHLY, LocalDate.now().plusMonths(1), "Entertainment");

        when(repository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SubscriptionResponse response = subscriptionService.createSubscription(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals("Netflix", response.name());
        assertEquals(new BigDecimal(15.99), response.price());
        assertEquals(BillingPeriod.MONTHLY, response.billingPeriod());
        assertEquals("Entertainment", response.category());
        verify(repository, times(1)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should update Subscription successfully")
    void testUpdateSubscription() {
        // Arrange
        UUID subscriptionId = UUID.randomUUID();
        SubscriptionRequest data = new SubscriptionRequest("Spotify", new BigDecimal(9.99), BillingPeriod.MONTHLY, LocalDate.now().plusMonths(1), "Music");
        Subscription existingSubscription = new Subscription();
        existingSubscription.setId(subscriptionId);
        existingSubscription.setName("Old Name");
        existingSubscription.setPrice(new BigDecimal(5.99));
        existingSubscription.setBillingPeriod(BillingPeriod.YEARLY);
        existingSubscription.setRenewalDate(LocalDate.now().plusMonths(6));
        existingSubscription.setCategory("Old Category");

        when(repository.findById(subscriptionId)).thenReturn(Optional.of(existingSubscription));
        when(repository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SubscriptionResponse response = subscriptionService.updateSubscription(subscriptionId, data);

        // Assert
        assertNotNull(response);
        assertEquals("Spotify", response.name());
        assertEquals(new BigDecimal(9.99), response.price());
        assertEquals(BillingPeriod.MONTHLY, response.billingPeriod());
        assertEquals("Music", response.category());
        verify(repository, times(1)).findById(subscriptionId);
        verify(repository, times(1)).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should delete Subscription successfully")
    void testDeleteSubscription() {
        UUID subscriptionId = UUID.randomUUID();
        Subscription existingSubscription = new Subscription();
        existingSubscription.setId(subscriptionId);
        when(repository.findById(subscriptionId)).thenReturn(Optional.of(existingSubscription));

        // Act
        subscriptionService.deleteSubscription(subscriptionId);

        // Assert
        verify(repository, times(1)).findById(subscriptionId);
        verify(repository, times(1)).delete(existingSubscription);
    }
}
