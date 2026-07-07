package com.qastore.api.category;

/*
 * ============================================================
 * File: CategoryHasActiveProductsException.java
 * Module: Category
 *
 * Responsibility:
 * Represents the business error thrown when trying to deactivate a category
 * that still has active products associated with it.
 *
 * Interaction:
 * CategoryJpaService throws this exception.
 * GlobalExceptionHandler converts it into an HTTP 409 Conflict response.
 *
 * Design Pattern:
 * Domain-specific Exception.
 *
 * Engineering Principles:
 * - Explicit business rule modeling.
 * - Data integrity: prevents active products from referencing inactive categories.
 * - API clarity: clients receive a meaningful business error.
 * ============================================================
 */

public class CategoryHasActiveProductsException extends RuntimeException {

    public CategoryHasActiveProductsException(Long categoryId) {
        super("Category with id " + categoryId + " cannot be deleted because it has active products");
    }
}