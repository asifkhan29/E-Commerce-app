package com.example.ecommerce_app.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce_app.entity.Product;
import com.example.ecommerce_app.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create a new product (ADMIN only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Product> createProduct(
            @Valid
            @RequestBody
            @Parameter(description = "Product payload", required = true)
            Product product
    ) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update product (ADMIN only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable("id") Long id,

            @Valid
            @RequestBody
            @Parameter(description = "Updated product payload", required = true)
            Product productDetails
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete product (ADMIN only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID", example = "1", required = true)
            @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Get all products with pagination & sorting")
    public ResponseEntity<Page<Product>> getAllProducts(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products with filters, pagination & sorting")
    public ResponseEntity<Page<Product>> searchProducts(
            @Parameter(description = "Product name (partial match)", example = "Laptop")
            @RequestParam(name = "name", required = false) String name,

            @Parameter(description = "Minimum price", example = "1000")
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,

            @Parameter(description = "Maximum price", example = "5000")
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,

            @Parameter(description = "Filter by stock availability", example = "true")
            @RequestParam(name = "inStock", required = false) Boolean inStock,

            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(
                size = 20,
                sort = "createdAt",
                direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                productService.searchProducts(name, minPrice, maxPrice, inStock, pageable)
        );
    }
}
