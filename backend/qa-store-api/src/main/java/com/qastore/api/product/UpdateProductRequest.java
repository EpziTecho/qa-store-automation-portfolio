package com.qastore.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/*
 * ============================================================
 * File: UpdateProductRequest.java
 * Module: Product
 *
 * Responsibility:
 * Represents the JSON request body required to update an existing product.
 *
 * Interaction:
 * ProductController receives this DTO from HTTP PUT /api/products/{id}.
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

@Schema(description = "Request body used to update an existing product")
public record UpdateProductRequest(

                @Schema(description = "Updated product name", example = "Updated Keyboard") @NotBlank(message = "Product name is required") String name,

                @Schema(description = "Updated product description", example = "Updated keyboard description") @NotBlank(message = "Product description is required") String description,

                @Schema(description = "Updated product price. Must be greater than zero.", example = "99.90") @NotNull(message = "Product price is required") @Positive(message = "Product price must be greater than zero") BigDecimal price,

                @Schema(description = "Updated available stock. Cannot be negative.", example = "30") @NotNull(message = "Product stock is required") @PositiveOrZero(message = "Product stock cannot be negative") Integer stock,

                @Schema(description = "Existing category ID associated with the product", example = "1") @NotNull(message = "Product category is required") Long categoryId) {
}