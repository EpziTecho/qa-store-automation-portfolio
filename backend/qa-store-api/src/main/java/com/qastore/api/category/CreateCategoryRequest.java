package com.qastore.api.category;

import io.swagger.v3.oas.annotations.media.Schema;
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

@Schema(description = "Request body used to create a category")
public record CreateCategoryRequest(

                @Schema(description = "Category name. Must be unique.", example = "Electronics") @NotBlank(message = "Category name is required") String name,

                @Schema(description = "Category description", example = "Electronic devices and accessories") @NotBlank(message = "Category description is required") String description) {
}