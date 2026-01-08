package com.example.ecommerce_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_app.dto.OrderRequest;
import com.example.ecommerce_app.dto.OrderResponse;
import com.example.ecommerce_app.entity.Order;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    Order getOrderById(Long id);

    Page<Order> getOrdersByUser(Long userId, Pageable pageable);

    Order cancelOrder(Long orderId);

    Page<Order> getAllOrders(Pageable pageable);
}
