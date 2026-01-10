package com.example.ecommerce_app.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
}
