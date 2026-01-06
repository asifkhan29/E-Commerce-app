package com.example.ecommerce_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentRequest {
    
    @NotBlank(message = "Order ID is required")
    private String razorpayOrderId;
    
    @NotBlank(message = "Payment ID is required")
    private String razorpayPaymentId;
    
    private String paymentMethod;
    private String upiId;
}