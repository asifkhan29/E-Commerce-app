package com.example.ecommerce_app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductSearchRequest {
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;
}