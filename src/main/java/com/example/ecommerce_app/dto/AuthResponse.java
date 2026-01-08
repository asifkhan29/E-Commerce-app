package com.example.ecommerce_app.dto;

import com.example.ecommerce_app.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    private String username;
    private String email;
    private User.UserRole role;
    private Long userId;
}