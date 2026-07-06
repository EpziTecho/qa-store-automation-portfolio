package com.qastore.api.product;

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
 * Provides a temporary in-memory implementation of ProductService.
 *
 * Interaction:
 * ProductController calls ProductService methods.
 * Spring injects this implementation because it is annotated with @Service.
 *
 * Design Pattern:
 * Service Layer.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: manages product operations for this phase.
 * - Dependency Inversion: implements ProductService abstraction.
 * - Thread-safety: uses ConcurrentHashMap and AtomicLong.
 * - Incremental design: will later be replaced by a database-backed implementation.
 * ============================================================
 */

@Service
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
                true);

        Product mouse = new Product(
                sequence.incrementAndGet(),
                "Wireless Mouse",
                "Wireless mouse for QA workstation",
                new BigDecimal("25.50"),
                50,
                true);

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
                true);

        products.put(id, product);

        return product;
    }
}