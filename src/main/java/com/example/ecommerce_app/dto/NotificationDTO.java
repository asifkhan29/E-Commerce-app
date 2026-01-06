package com.example.ecommerce_app.dto;

import java.time.LocalDateTime;

import com.example.ecommerce_app.enums.NotificationLevel;
import com.example.ecommerce_app.enums.NotificationStatus;
import com.example.ecommerce_app.enums.NotificationType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationLevel level;
    private NotificationStatus status;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}