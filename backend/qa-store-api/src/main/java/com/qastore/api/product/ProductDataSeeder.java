package com.qastore.api.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/*
 * ============================================================
 * File: ProductDataSeeder.java
 * Module: Product Seed Data
 *
 * Responsibility:
 * Inserts initial product records when the database is empty.
 *
 * Interaction:
 * Runs during Spring Boot startup and uses ProductRepository to persist
 * default products in MySQL.
 *
 * Design Pattern:
 * Application Data Seeder.
 *
 * Engineering Principles:
 * - Developer experience: provides initial data for manual API testing.
 * - Idempotency: only inserts records when the table is empty.
 * - Testability: creates predictable initial data for local exploration.
 * ============================================================
 */

@Component
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductDataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        ProductEntity laptop = new ProductEntity(
                "Laptop QA Pro",
                "Laptop designed for automation testing practice",
                new BigDecimal("1200.00"),
                10,
                true);

        ProductEntity mouse = new ProductEntity(
                "Wireless Mouse",
                "Wireless mouse for QA workstation",
                new BigDecimal("25.50"),
                50,
                true);

        productRepository.save(laptop);
        productRepository.save(mouse);
    }
}