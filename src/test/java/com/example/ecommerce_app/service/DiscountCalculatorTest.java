package com.example.ecommerce_app.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ecommerce_app.entity.User;
import com.example.ecommerce_app.service.strategy.DiscountCalculator;
import com.example.ecommerce_app.service.strategy.DiscountStrategy;

@ExtendWith(MockitoExtension.class)
class DiscountCalculatorTest {
    
    @Mock
    private DiscountStrategy noDiscountStrategy;
    
    @Mock
    private DiscountStrategy premiumUserStrategy;
    
    @Mock
    private DiscountStrategy highValueStrategy;
    
    private DiscountCalculator discountCalculator;
    
    private User regularUser;
    private User premiumUser;
    
    @BeforeEach
    void setUp() {
        regularUser = new User();
        regularUser.setId(1L);
        regularUser.setRole(User.UserRole.USER);
        
        premiumUser = new User();
        premiumUser.setId(2L);
        premiumUser.setRole(User.UserRole.PREMIUM_USER);
        
        discountCalculator = new DiscountCalculator(Arrays.asList(
            noDiscountStrategy, premiumUserStrategy, highValueStrategy
        ));
    }
    
    @Test
    void calculateTotalDiscount_RegularUser_SmallOrder() {
        when(noDiscountStrategy.calculateDiscount(any(), eq(regularUser)))
            .thenReturn(BigDecimal.ZERO);
        when(noDiscountStrategy.getStrategyName()).thenReturn("No Discount");
        
        when(premiumUserStrategy.calculateDiscount(any(), eq(regularUser)))
            .thenReturn(BigDecimal.ZERO);
        when(premiumUserStrategy.getStrategyName()).thenReturn("Premium Discount");
        
        when(highValueStrategy.calculateDiscount(any(), eq(regularUser)))
            .thenReturn(BigDecimal.ZERO);
        when(highValueStrategy.getStrategyName()).thenReturn("High Value Discount");
        
        DiscountCalculator.DiscountCalculationResult result = 
            discountCalculator.calculateTotalDiscount(new BigDecimal("100"), regularUser);
        
        assertEquals(new BigDecimal("100"), result.getOrderTotal());
        assertEquals(BigDecimal.ZERO, result.getTotalDiscount());
        assertEquals(new BigDecimal("100"), result.getFinalAmount());
        assertTrue(result.getAppliedDiscounts().isEmpty());
    }
    
    @Test
    void calculateTotalDiscount_PremiumUser_LargeOrder() {
        when(noDiscountStrategy.calculateDiscount(any(), eq(premiumUser)))
            .thenReturn(BigDecimal.ZERO);
        when(noDiscountStrategy.getStrategyName()).thenReturn("No Discount");
        
        when(premiumUserStrategy.calculateDiscount(any(), eq(premiumUser)))
            .thenReturn(new BigDecimal("60.00"));
        when(premiumUserStrategy.getStrategyName()).thenReturn("Premium Discount");
        
        when(highValueStrategy.calculateDiscount(any(), eq(premiumUser)))
            .thenReturn(new BigDecimal("30.00"));
        when(highValueStrategy.getStrategyName()).thenReturn("High Value Discount");
        
        DiscountCalculator.DiscountCalculationResult result = 
            discountCalculator.calculateTotalDiscount(new BigDecimal("600"), premiumUser);
        
        assertEquals(new BigDecimal("600"), result.getOrderTotal());
        assertEquals(new BigDecimal("90.00"), result.getTotalDiscount());
        assertEquals(new BigDecimal("510.00"), result.getFinalAmount());
        assertEquals(2, result.getAppliedDiscounts().size());
    }
}