package com.example.ecommerce_app.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.ecommerce_app.dto.OrderRequest;
import com.example.ecommerce_app.entity.Product;
import com.example.ecommerce_app.entity.User;
import com.example.ecommerce_app.exception.model.InsufficientStockException;
import com.example.ecommerce_app.repository.OrderRepository;
import com.example.ecommerce_app.repository.UserRepository;
import com.example.ecommerce_app.service.strategy.DiscountCalculator;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private ProductService productService;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private DiscountCalculator discountCalculator;
    
    @InjectMocks
    private OrderService orderService;
    
    private User testUser;
    private Product testProduct;
    private OrderRequest orderRequest;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setRole(User.UserRole.USER);
        
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(10);
        
        OrderRequest.OrderItemRequest itemRequest = new OrderRequest.OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);
        
        orderRequest = new OrderRequest();
        orderRequest.setOrderItems(Arrays.asList(itemRequest));
    }
    
    @Test
    void createOrder_Success() {
        try (MockedStatic<SecurityContextHolder> mockedSecurity = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("testuser");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(productService.getProductById(1L)).thenReturn(testProduct);
            when(discountCalculator.calculateTotalDiscount(any(), eq(testUser)))
                .thenReturn(new DiscountCalculator.DiscountCalculationResult());
            
            orderService.createOrder(orderRequest);
            
            verify(productService, times(1)).decreaseStock(1L, 2);
            verify(orderRepository, times(1)).save(any());
        }
    }
    
    @Test
    void createOrder_InsufficientStock() {
        try (MockedStatic<SecurityContextHolder> mockedSecurity = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            
            mockedSecurity.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn("testuser");
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            
            testProduct.setQuantity(1);
            when(productService.getProductById(1L)).thenReturn(testProduct);
            
            assertThrows(InsufficientStockException.class, () -> {
                orderService.createOrder(orderRequest);
            });
        }
    }
}