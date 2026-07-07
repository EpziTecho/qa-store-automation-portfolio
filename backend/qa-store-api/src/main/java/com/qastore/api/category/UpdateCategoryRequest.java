package com.qastore.api.category;

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "Request body used to update an existing category")
public record UpdateCategoryRequest(

                @Schema(description = "Updated category name. Must not be used by another category.", example = "Updated Electronics") @NotBlank(message = "Category name is required") String name,

                @Schema(description = "Updated category description", example = "Updated category description") @NotBlank(message = "Category description is required") String description) {
}