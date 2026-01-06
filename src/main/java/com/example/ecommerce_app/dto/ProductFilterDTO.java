package com.example.ecommerce_app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilterDTO {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}