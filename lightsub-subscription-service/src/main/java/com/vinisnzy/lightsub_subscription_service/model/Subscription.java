package com.vinisnzy.lightsub_subscription_service.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.vinisnzy.lightsub_subscription_service.model.dtos.BillingPeriod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private BillingPeriod billingPeriod;

  @Column(nullable = false)
  private LocalDate renewalDate;

  @Column(nullable = false)
  private String category;

  private LocalDateTime createdAt = LocalDateTime.now();
}
