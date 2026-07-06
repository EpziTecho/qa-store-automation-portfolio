package com.qastore.api.product;

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

public record CreateProductRequest(

        @NotBlank(message = "Product name is required") String name,

        @NotBlank(message = "Product description is required") String description,

        @NotNull(message = "Product price is required") @Positive(message = "Product price must be greater than zero") BigDecimal price,

        @NotNull(message = "Product stock is required") @PositiveOrZero(message = "Product stock cannot be negative") Integer stock) {
}