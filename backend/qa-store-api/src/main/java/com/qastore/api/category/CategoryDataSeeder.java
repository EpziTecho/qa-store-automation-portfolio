package com.qastore.api.category;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
 * ============================================================
 * File: CategoryDataSeeder.java
 * Module: Category Seed Data
 *
 * Responsibility:
 * Inserts initial category records when the database is empty.
 *
 * Interaction:
 * Runs during Spring Boot startup and uses CategoryRepository to persist
 * default categories in MySQL.
 *
 * Design Pattern:
 * Application Data Seeder.
 *
 * Engineering Principles:
 * - Developer experience: provides initial data for manual API testing.
 * - Idempotency: only inserts records when the table is empty.
 * - Predictability: creates stable categories for local exploration.
 * ============================================================
 */

@Component
@Order(1)
public class CategoryDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryDataSeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return;
        }

        categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        categoryRepository.save(new CategoryEntity(
                "Accessories",
                "General workstation accessories",
                true));
    }
}