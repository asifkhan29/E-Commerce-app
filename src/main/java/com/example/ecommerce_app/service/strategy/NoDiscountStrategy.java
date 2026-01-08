package com.example.ecommerce_app.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.ecommerce_app.entity.User;

@Component
public class NoDiscountStrategy implements DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal orderTotal, User user) {
        return BigDecimal.ZERO;
    }
    
    @Override
    public String getStrategyName() {
        return "No Discount";
    }
}