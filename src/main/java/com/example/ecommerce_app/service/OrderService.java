package com.example.ecommerce_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_app.dto.OrderRequest;
import com.example.ecommerce_app.dto.OrderResponse;
import com.example.ecommerce_app.entity.Order;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long id);

    Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable);

    OrderResponse cancelOrder(Long orderId);

    Page<OrderResponse> getAllOrders(Pageable pageable);
}
