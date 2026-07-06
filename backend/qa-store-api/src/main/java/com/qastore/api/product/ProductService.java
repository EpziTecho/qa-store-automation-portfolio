package com.qastore.api.product;

import java.util.List;

/*
 * ============================================================
 * File: ProductService.java
 * Module: Product
 *
 * Responsibility:
 * Defines the business operations available for products.
 *
 * Interaction:
 * ProductController depends on this interface instead of a concrete implementation.
 * InMemoryProductService implements this contract for the current phase.
 *
 * Design Pattern:
 * Service Layer + Dependency Inversion.
 *
 * Engineering Principles:
 * - Dependency Inversion Principle: controllers depend on abstractions.
 * - Interface Segregation: only product-related operations are exposed.
 * - Testability: implementations can be replaced or mocked.
 * ============================================================
 */

public interface ProductService {

    List<Product> findAll();

    Product findById(Long id);

    Product create(CreateProductRequest request);
}