package com.qastore.api.category;

/*
 * ============================================================
 * File: Category.java
 * Module: Category
 *
 * Responsibility:
 * Represents the internal category domain model used by the service layer.
 *
 * Interaction:
 * CategoryJpaService works with CategoryEntity and maps it into this domain model.
 * CategoryController later converts this model into CategoryResponse.
 *
 * Design Pattern:
 * Domain Model.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: represents category data only.
 * - Immutability: Java records are immutable by default.
 * - Separation of concerns: domain model is separated from persistence entity.
 * ============================================================
 */

public record Category(
        Long id,
        String name,
        String description,
        Boolean active) {
}