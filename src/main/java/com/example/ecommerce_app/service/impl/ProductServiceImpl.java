package com.example.ecommerce_app.service.impl;

import java.math.BigDecimal;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_app.entity.Product;
import com.example.ecommerce_app.exception.model.ResourceNotFoundException;
import com.example.ecommerce_app.repository.ProductRepository;
import com.example.ecommerce_app.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
//  @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating prodoct: {}", product.getName());
        return productRepository.save(product);
    }

    @Override
//  @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
//  @CacheEvict(value = {"products", "productsByPage"}, key = "#id")
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());

        return productRepository.save(product);
    }

    @Override
//  @CacheEvict(value = {"products", "productsByPage"}, allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
//  @Cacheable("productsByPage")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findByIsDeletedFalse(pageable);
    }

    @Override
//  @Cacheable("productsSearch")
    public Page<Product> searchProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Pageable pageable) {

        Specification<Product> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, q, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (minPrice != null) {
            spec = spec.and((root, q, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, q, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (Boolean.TRUE.equals(inStock)) {
            spec = spec.and((root, q, cb) ->
                    cb.greaterThan(root.get("quantity"), 0));
        }

        spec = spec.and((root, q, cb) ->
                cb.equal(root.get("isDeleted"), false));

        return productRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}
