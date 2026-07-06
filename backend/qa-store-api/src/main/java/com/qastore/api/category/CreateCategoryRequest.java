package com.qastore.api.category;

import jakarta.validation.constraints.NotBlank;

/*
 * ============================================================
 * File: CreateCategoryRequest.java
 * Module: Category
 *
 * Responsibility:
 * Represents the JSON request body required to create a category.
 *
 * Interaction:
 * CategoryController receives this DTO from HTTP POST /api/categories.
 * Bean Validation validates the fields before the service layer is executed.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Separation of concerns: external API input is separated from internal model.
 * - Fail fast: invalid requests are rejected before reaching persistence logic.
 * - Declarative validation: constraints are defined close to the API contract.
 * ============================================================
 */

public record CreateCategoryRequest(

        @NotBlank(message = "Category name is required") String name,

        @NotBlank(message = "Category description is required") String description) {
}