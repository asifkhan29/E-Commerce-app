package com.example.ecommerce_app.service.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce_app.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountCalculator {
    
    private final List<DiscountStrategy> discountStrategies;
    
    public DiscountCalculationResult calculateTotalDiscount(BigDecimal orderTotal, User user) {
        DiscountCalculationResult result = new DiscountCalculationResult();
        result.setOrderTotal(orderTotal);
        
        for (DiscountStrategy strategy : discountStrategies) {
            BigDecimal discount = strategy.calculateDiscount(orderTotal, user);
            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                result.addAppliedDiscount(strategy.getStrategyName(), discount);
            }
        }
        
        // Cap discount at order total
        if (result.getTotalDiscount().compareTo(orderTotal) > 0) {
            result.setTotalDiscount(orderTotal);
        }
        
        result.setFinalAmount(orderTotal.subtract(result.getTotalDiscount()));
        
        log.info("Discount calculation for user {} (Role: {}): Total: ${}, Discount: ${}, Final: ${}",
            user.getId(), user.getRole(), orderTotal, result.getTotalDiscount(), result.getFinalAmount());
        
        return result;
    }
    
    @Data
    public static class DiscountCalculationResult {
        private BigDecimal orderTotal;
        private BigDecimal totalDiscount = BigDecimal.ZERO;
        private BigDecimal finalAmount;
        private List<AppliedDiscount> appliedDiscounts = new ArrayList<>();
        
        public void addAppliedDiscount(String strategyName, BigDecimal discount) {
            this.appliedDiscounts.add(new AppliedDiscount(strategyName, discount));
            this.totalDiscount = this.totalDiscount.add(discount);
        }
        
        @Data
        @AllArgsConstructor
        public static class AppliedDiscount {
            private String strategyName;
            private BigDecimal discountAmount;
        }
    }
}