package com.qastore.api.product;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/*
 * ============================================================
 * File: InMemoryProductService.java
 * Module: Product
 *
 * Responsibility:
 * Provides an alternative in-memory implementation of ProductService.
 *
 * Interaction:
 * ProductController depends on ProductService.
 * This implementation is only enabled when the Spring profile "in-memory"
 * is active.
 *
 * Design Pattern:
 * Service Layer.
 *
 * Engineering Principles:
 * - Dependency Inversion: implements ProductService abstraction.
 * - Replaceable implementation: can be swapped with ProductJpaService.
 * - Profile-based configuration: avoids bean conflicts with the JPA service.
 * - Testability: useful for isolated local executions without database access.
 * ============================================================
 */

@Service
@Profile("in-memory")
public class InMemoryProductService implements ProductService {

    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public InMemoryProductService() {
        Product laptop = new Product(
                sequence.incrementAndGet(),
                "Laptop QA Pro",
                "Laptop designed for automation testing practice",
                new BigDecimal("1200.00"),
                10,
                true,
                1L,
                "Electronics");

        Product mouse = new Product(
                sequence.incrementAndGet(),
                "Wireless Mouse",
                "Wireless mouse for QA workstation",
                new BigDecimal("25.50"),
                50,
                true,
                2L,
                "Accessories");

        products.put(laptop.id(), laptop);
        products.put(mouse.id(), mouse);
    }

    @Override
    public List<Product> findAll() {
        return products.values()
                .stream()
                .sorted(Comparator.comparing(Product::id))
                .toList();
    }

    @Override
    public Product findById(Long id) {
        Product product = products.get(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    @Override
    public Product create(CreateProductRequest request) {
        Long id = sequence.incrementAndGet();

        Product product = new Product(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                true,
                request.categoryId(),
                resolveCategoryName(request.categoryId()));

        products.put(id, product);

        return product;
    }

    /*
     * Resolves a category name for the in-memory profile.
     *
     * This implementation does not access MySQL, so it uses a controlled
     * local mapping only for development or isolated executions.
     */
    private String resolveCategoryName(Long categoryId) {
        if (categoryId == null) {
            return "Unassigned";
        }

        if (categoryId.equals(1L)) {
            return "Electronics";
        }

        if (categoryId.equals(2L)) {
            return "Accessories";
        }

        return "In-Memory Category";
    }
}