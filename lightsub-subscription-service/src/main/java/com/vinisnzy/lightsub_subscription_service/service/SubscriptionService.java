package com.vinisnzy.lightsub_subscription_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionRequest;
import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionResponse;
import com.vinisnzy.lightsub_subscription_service.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public List<SubscriptionResponse> getAllSubscriptions(UUID userId) {
    return repository.findByUserId(userId).stream()
        .map(SubscriptionResponse::fromEntity)
        .toList();
  }

  public SubscriptionResponse getSubscriptionById(UUID subscriptionId) {
    return repository.findById(subscriptionId)
        .map(SubscriptionResponse::fromEntity)
        .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
  }

  public SubscriptionResponse createSubscription(UUID userId, SubscriptionRequest data) {
    var subscription = repository.save(SubscriptionRequest.toEntity(userId,data));
    return SubscriptionResponse.fromEntity(subscription);
  }

  public SubscriptionResponse updateSubscription(UUID subscriptionId, SubscriptionRequest data) {
    var subscription = repository.findById(subscriptionId)
        .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));

    subscription.setName(data.name());
    subscription.setPrice(data.price());
    subscription.setBillingPeriod(data.billingPeriod());
    subscription.setRenewalDate(data.renewalDate());
    subscription.setCategory(data.category());

    var updatedSubscription = repository.save(subscription);
    return SubscriptionResponse.fromEntity(updatedSubscription);
  }

  public void deleteSubscription(UUID subscriptionId) {
    var subscription = repository.findById(subscriptionId)
        .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + subscriptionId));
    repository.delete(subscription);
  }
}
