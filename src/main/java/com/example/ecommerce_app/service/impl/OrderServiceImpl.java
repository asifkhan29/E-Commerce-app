package com.example.ecommerce_app.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_app.dto.OrderRequest;
import com.example.ecommerce_app.dto.OrderResponse;
import com.example.ecommerce_app.entity.Order;
import com.example.ecommerce_app.entity.Order.OrderStatus;
import com.example.ecommerce_app.entity.OrderItem;
import com.example.ecommerce_app.entity.Product;
import com.example.ecommerce_app.entity.User;
import com.example.ecommerce_app.exception.model.InsufficientStockException;
import com.example.ecommerce_app.exception.model.ResourceNotFoundException;
import com.example.ecommerce_app.repository.OrderRepository;
import com.example.ecommerce_app.repository.UserRepository;
import com.example.ecommerce_app.service.OrderService;
import com.example.ecommerce_app.service.ProductService;
import com.example.ecommerce_app.service.strategy.DiscountCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final DiscountCalculator discountCalculator;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Creting order for user:{}", user.getId());

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);

        BigDecimal orderTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getQuantity() +
                        ", Requested: " + itemRequest.getQuantity()
                );
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setDiscountApplied(order.getDiscountAmount());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            orderItem.setTotalPrice(itemTotal);

            orderItems.add(orderItem);
            orderTotal = orderTotal.add(itemTotal);
        }

        DiscountCalculator.DiscountCalculationResult discountResult =
                discountCalculator.calculateTotalDiscount(orderTotal, user);

        order.setOrderItems(orderItems);
        order.setOrderTotal(orderTotal);
        order.setDiscountAmount(discountResult.getTotalDiscount());
        order.setFinalAmount(discountResult.getFinalAmount());
        order.setStatus(Order.OrderStatus.PENDING);

        for (OrderItem item : orderItems) {
            productService.decreaseStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }

        Order savedOrder = orderRepository.save(order);

        log.info(
                "Order created successfully with id: {}, Total: {}, Discount: {}, Final: {}",
                savedOrder.getId(),
                orderTotal,
                discountResult.getTotalDiscount(),
                discountResult.getFinalAmount()
        );

        return OrderResponse.builder()
                .id(savedOrder.getId())
                .totalAmount(savedOrder.getOrderTotal())
                .discountAmount(savedOrder.getDiscountAmount())
                .finalAmount(savedOrder.getFinalAmount())
                .createdAt(savedOrder.getCreatedAt())
                .username(savedOrder.getUser().getUsername())
                .build();
    }

    @Override
//    @Cacheable(value = "orders", key = "#id")
    public Order getOrderById(Long id) {
        log.debug("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
//    @Cacheable(value = "userOrders", key = "#userId + '-' + #pageable.pageNumber + '-' + #pageable.sort")
    public Page<Order> getOrdersByUser(Long userId, Pageable pageable) {
        log.debug("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
//    @CacheEvict(value = {"orders", "userOrders"}, allEntries = true)
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!order.getUser().getId().equals(user.getId())
                && user.getRole() != User.UserRole.ADMIN) {
            throw new SecurityException("Not authorized to cancel this order");
        }

        if (order.getStatus() == Order.OrderStatus.CONFIRMED) {
            for (OrderItem item : order.getOrderItems()) {
                productService.increaseStock(
                        item.getProduct().getId(),
                        item.getQuantity()
                );
            }
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);

        log.info("Order canceled with id:{}", orderId);
        return updatedOrder;
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        log.debug("Fetching all orders");
        return orderRepository.findAll(pageable);
    }
}
