package com.vinisnzy.lightsub_subscription_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionRequest;
import com.vinisnzy.lightsub_subscription_service.dtos.SubscriptionResponse;
import com.vinisnzy.lightsub_subscription_service.service.SubscriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions(@RequestHeader("X-User-Id") String userId) {
        var subscriptions = service.getAllSubscriptions(UUID.fromString(userId));
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable UUID id) {
        var subscription = service.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody SubscriptionRequest data) {
        var subscription = service.createSubscription(UUID.fromString(userId), data);
        return ResponseEntity.ok(subscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable UUID id,
            @Valid @RequestBody SubscriptionRequest data) {
        var subscription = service.updateSubscription(id, data);
        return ResponseEntity.ok(subscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
