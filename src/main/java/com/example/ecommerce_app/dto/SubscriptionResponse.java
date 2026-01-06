package com.example.ecommerce_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ecommerce_app.enums.SubscriptionStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {
    private Long id;
    private String subscriptionId;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime nextPaymentDate;
    private LocalDateTime endDate;
    private BigDecimal monthlyAmount;
    private String upiId;
}