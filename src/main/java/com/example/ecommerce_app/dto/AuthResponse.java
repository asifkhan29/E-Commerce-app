package com.example.ecommerce_app.dto;

import com.example.ecommerce_app.enums.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private UserRole role;
    private Boolean isPremium;
}