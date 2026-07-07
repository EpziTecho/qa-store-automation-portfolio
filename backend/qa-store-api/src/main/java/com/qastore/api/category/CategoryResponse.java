package com.qastore.api.category;

import io.swagger.v3.oas.annotations.media.Schema;

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

@Schema(description = "Category response returned by the API")
public record CategoryResponse(

        @Schema(description = "Category ID", example = "1") Long id,

        @Schema(description = "Category name", example = "Electronics") String name,

        @Schema(description = "Category description", example = "Electronic devices and accessories") String description,

        @Schema(description = "Indicates whether the category is active", example = "true") Boolean active) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.id(),
                category.name(),
                category.description(),
                category.active());
    }
}