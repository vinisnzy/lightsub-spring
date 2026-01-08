package com.vinisnzy.lightsub_billing_service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.vinisnzy.lightsub_billing_service.client.dto.SubscriptionResponse;

@FeignClient(name = "lightsub-subscriptions-service", url = "${subscriptions.service.url}", path = "/subscriptions")
public interface SubscriptionsClient {

    @GetMapping
    List<SubscriptionResponse> getAllSubscriptions(@RequestHeader("X-User-Id") String userId);
}
