package com.qastore.api.category;

import java.util.List;

/*
 * ============================================================
 * File: CategoryService.java
 * Module: Category
 *
 * Responsibility:
 * Defines the business operations available for categories.
 *
 * Interaction:
 * CategoryController depends on this interface instead of a concrete implementation.
 * CategoryJpaService implements this contract using MySQL through JPA.
 *
 * Design Pattern:
 * Service Layer + Dependency Inversion.
 *
 * Engineering Principles:
 * - Dependency Inversion Principle: controllers depend on abstractions.
 * - Interface Segregation: only category-related operations are exposed.
 * - Testability: implementations can be replaced or mocked.
 * ============================================================
 */

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category create(CreateCategoryRequest request);

    Category update(Long id, UpdateCategoryRequest request);

    void delete(Long id);
}