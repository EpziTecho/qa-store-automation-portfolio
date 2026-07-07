package com.qastore.api.category;

/*
 * ============================================================
 * File: CategoryResponse.java
 * Module: Category
 *
 * Responsibility:
 * Represents the JSON response returned by category endpoints.
 *
 * Interaction:
 * CategoryController returns this DTO to API clients.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - API contract clarity.
 * - Separation between internal model and external response.
 * - Immutability through Java records.
 * ============================================================
 */

public record CategoryResponse(
        Long id,
        String name,
        String description,
        Boolean active) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.id(),
                category.name(),
                category.description(),
                category.active());
    }
}