package com.example.ecommerce_app.service.strategy;

import java.math.BigDecimal;

import com.example.ecommerce_app.entity.User;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(BigDecimal orderTotal, User user);
    String getStrategyName();
}