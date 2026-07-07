package com.qastore.api.product;

import com.qastore.api.category.CategoryEntity;
import com.qastore.api.category.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
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
 * Runs during Spring Boot startup after CategoryDataSeeder.
 * Uses ProductRepository to persist default products in MySQL.
 * Uses CategoryRepository to associate products with existing categories.
 *
 * Design Pattern:
 * Application Data Seeder.
 *
 * Engineering Principles:
 * - Developer experience: provides initial data for manual API testing.
 * - Idempotency: only inserts records when the table is empty.
 * - Referential integrity: seeded products are assigned to real categories.
 * ============================================================
 */

@Component
@Order(2)
public class ProductDataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDataSeeder(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        CategoryEntity electronics = categoryRepository.findByNameIgnoreCase("Electronics")
                .orElseThrow(() -> new IllegalStateException("Default category Electronics was not found"));

        CategoryEntity accessories = categoryRepository.findByNameIgnoreCase("Accessories")
                .orElseThrow(() -> new IllegalStateException("Default category Accessories was not found"));

        ProductEntity laptop = new ProductEntity(
                "Laptop QA Pro",
                "Laptop designed for automation testing practice",
                new BigDecimal("1200.00"),
                10,
                true,
                electronics);

        ProductEntity mouse = new ProductEntity(
                "Wireless Mouse",
                "Wireless mouse for QA workstation",
                new BigDecimal("25.50"),
                50,
                true,
                accessories);

        productRepository.save(laptop);
        productRepository.save(mouse);
    }
}