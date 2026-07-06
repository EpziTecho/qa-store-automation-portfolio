package com.qastore.api.category;

import jakarta.validation.constraints.NotBlank;

/*
 * ============================================================
 * File: UpdateCategoryRequest.java
 * Module: Category
 *
 * Responsibility:
 * Represents the JSON request body required to update an existing category.
 *
 * Interaction:
 * CategoryController receives this DTO from HTTP PUT /api/categories/{id}.
 * Bean Validation validates the fields before the service layer executes.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Separation of concerns: update input contract is explicit.
 * - Fail fast: invalid requests are rejected before reaching business logic.
 * - API clarity: create and update contracts are separated.
 * ============================================================
 */

public record UpdateCategoryRequest(

        @NotBlank(message = "Category name is required") String name,

        @NotBlank(message = "Category description is required") String description) {
}