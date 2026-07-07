package com.qastore.api.category;

/*
 * ============================================================
 * File: DuplicateCategoryException.java
 * Module: Category
 *
 * Responsibility:
 * Represents the business error thrown when a category name already exists.
 *
 * Interaction:
 * CategoryJpaService throws this exception before persisting duplicated data.
 * GlobalExceptionHandler converts it into an HTTP 409 Conflict response.
 *
 * Design Pattern:
 * Domain-specific Exception.
 *
 * Engineering Principles:
 * - Explicit business rule modeling.
 * - Fail fast: duplicate data is rejected before database insertion.
 * - API clarity: clients receive a meaningful error instead of a generic DB error.
 * ============================================================
 */

public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String name) {
        super("Category with name '" + name + "' already exists");
    }
}