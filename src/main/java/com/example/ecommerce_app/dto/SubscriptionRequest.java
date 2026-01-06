package com.example.ecommerce_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscriptionRequest {
    
    @NotBlank(message = "UPI ID is required")
    private String upiId;
}