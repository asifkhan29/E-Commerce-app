package com.example.ecommerce_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.ecommerce_app.entity.Order.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
   
    private String username;
}
