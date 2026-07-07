package com.qastore.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/*
 * ============================================================
 * File: CreateProductRequest.java
 * Module: Product
 *
 * Responsibility:
 * Represents the JSON request body required to create a product.
 *
 * Interaction:
 * ProductController receives this DTO from HTTP POST /api/products.
 * Bean Validation validates the fields before the service layer is executed.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Separation of concerns: external API input is separated from internal model.
 * - Fail fast: invalid requests are rejected before reaching business logic.
 * - Declarative validation: constraints are defined close to the data contract.
 * ============================================================
 */

@Schema(description = "Request body used to create a product")
public record CreateProductRequest(

        @Schema(description = "Product name", example = "Mechanical Keyboard") @NotBlank(message = "Product name is required") String name,

        @Schema(description = "Product description", example = "Keyboard for automation engineers") @NotBlank(message = "Product description is required") String description,

        @Schema(description = "Product price. Must be greater than zero.", example = "85.90") @NotNull(message = "Product price is required") @Positive(message = "Product price must be greater than zero") BigDecimal price,

        @Schema(description = "Available product stock. Cannot be negative.", example = "20") @NotNull(message = "Product stock is required") @PositiveOrZero(message = "Product stock cannot be negative") Integer stock,

        @Schema(description = "Existing category ID associated with the product", example = "1") @NotNull(message = "Product category is required") Long categoryId) {
}