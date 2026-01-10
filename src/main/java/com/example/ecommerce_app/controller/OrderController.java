package com.example.ecommerce_app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce_app.dto.OrderRequest;
import com.example.ecommerce_app.dto.OrderResponse;
import com.example.ecommerce_app.entity.Order;
import com.example.ecommerce_app.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER')")
    @Operation(
        summary = "Place a new order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid
            @RequestBody
            @Parameter(description = "Order request payload", required = true)
            OrderRequest orderRequest
    ) {
        log.info("Placing new order");
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    @Operation(
        summary = "Get order by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order ID", example = "101", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    @Operation(
        summary = "Get orders of a specific user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<OrderResponse>> getUserOrders(
            @Parameter(description = "User ID", example = "10", required = true)
            @PathVariable("userId") Long userId,

            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, pageable));
    }

    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get all orders (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'PREMIUM_USER', 'ADMIN')")
    @Operation(
        summary = "Cancel an order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<OrderResponse> cancelOrder(
            @Parameter(description = "Order ID", example = "101", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
