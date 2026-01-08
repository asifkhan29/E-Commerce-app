package com.example.ecommerce_app.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String productName, Integer available, Integer requested) {
        super(String.format("Insufficient stock for product: %s. Available: %d, Requested: %d",
            productName, available, requested));
    }
    
    public InsufficientStockException(Long productId, Integer available, Integer requested) {
        super(String.format("Insufficient stock for product ID: %d. Available: %d, Requested: %d",
            productId, available, requested));
    }
}