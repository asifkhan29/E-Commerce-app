package com.example.ecommerce_app.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.ecommerce_app.entity.User;

@Component
public class PremiumUserDiscountStrategy implements DiscountStrategy {
    
    private static final BigDecimal PREMIUM_DISCOUNT_PERCENTAGE = new BigDecimal("0.10");
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal, User user) {
        if (user.getRole() == User.UserRole.PREMIUM_USER) {
            return orderTotal.multiply(PREMIUM_DISCOUNT_PERCENTAGE);
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public String getStrategyName() {
        return "Premium User Discount (10%)";
    }
}