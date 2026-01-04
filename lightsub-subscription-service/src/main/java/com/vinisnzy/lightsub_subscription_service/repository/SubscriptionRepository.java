package com.vinisnzy.lightsub_subscription_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vinisnzy.lightsub_subscription_service.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
