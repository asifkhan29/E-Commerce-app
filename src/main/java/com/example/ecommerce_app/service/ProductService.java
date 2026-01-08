package com.example.ecommerce_app.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ecommerce_app.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(Long id);

    Product updateProduct(Long id, Product productDetails);

    void deleteProduct(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> searchProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Pageable pageable
    );

    void decreaseStock(Long productId, Integer quantity);

    void increaseStock(Long productId, Integer quantity);
}
