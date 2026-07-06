package com.qastore.api.product;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * ============================================================
 * File: ProductJpaService.java
 * Module: Product Persistence Service
 *
 * Responsibility:
 * Provides a database-backed implementation of ProductService using MySQL.
 *
 * Interaction:
 * ProductController depends on ProductService.
 * Spring injects this implementation by default because it is registered as a service.
 * ProductJpaService delegates persistence operations to ProductRepository.
 *
 * Design Pattern:
 * Service Layer + Repository Pattern.
 *
 * Engineering Principles:
 * - Dependency Inversion: implements ProductService abstraction.
 * - Separation of concerns: business operations are separated from HTTP handling.
 * - Transaction management: database operations are executed inside transactions.
 * - Replaceable implementation: replaces the previous in-memory implementation.
 * ============================================================
 */

@Service
@Profile("!in-memory")
@Transactional
public class ProductJpaService implements ProductService {

    private final ProductRepository productRepository;

    public ProductJpaService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAllByOrderByIdAsc()
                .stream()
                .map(ProductMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(CreateProductRequest request) {
        ProductEntity entity = ProductMapper.toEntity(request);

        ProductEntity savedEntity = productRepository.save(entity);

        return ProductMapper.toDomain(savedEntity);
    }
}