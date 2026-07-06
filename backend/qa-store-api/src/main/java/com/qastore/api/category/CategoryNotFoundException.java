package com.qastore.api.category;

/*
 * ============================================================
 * File: CategoryNotFoundException.java
 * Module: Category
 *
 * Responsibility:
 * Represents the business error thrown when a category does not exist.
 *
 * Interaction:
 * CategoryJpaService throws this exception.
 * GlobalExceptionHandler converts it into an HTTP 404 response.
 *
 * Design Pattern:
 * Domain-specific Exception.
 *
 * Engineering Principles:
 * - Explicit error modeling.
 * - Separation of concerns: service throws business error, web layer maps it.
 * - Clean Code: avoids returning null when a category is not found.
 * ============================================================
 */

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super("Category with id " + id + " was not found");
    }
}