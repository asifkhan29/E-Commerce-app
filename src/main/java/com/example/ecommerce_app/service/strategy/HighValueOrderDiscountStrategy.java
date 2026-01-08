package com.example.ecommerce_app.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.ecommerce_app.entity.User;

@Component
public class HighValueOrderDiscountStrategy implements DiscountStrategy {
    
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("500");
    private static final BigDecimal HIGH_VALUE_DISCOUNT_PERCENTAGE = new BigDecimal("0.05");
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal, User user) {
        if (orderTotal.compareTo(HIGH_VALUE_THRESHOLD) > 0) {
            return orderTotal.multiply(HIGH_VALUE_DISCOUNT_PERCENTAGE);
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String getStrategyName() {
        return "High Value Order Discount (5%)";
    }
}