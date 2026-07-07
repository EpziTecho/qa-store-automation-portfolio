package com.qastore.api.product;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/*
 * ============================================================
 * File: ProductResponse.java
 * Module: Product
 *
 * Responsibility:
 * Represents the JSON response returned by product endpoints.
 *
 * Interaction:
 * ProductController returns this DTO to API clients.
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

@Schema(description = "Product response returned by the API")
public record ProductResponse(

        @Schema(description = "Product ID", example = "1") Long id,

        @Schema(description = "Product name", example = "Mechanical Keyboard") String name,

        @Schema(description = "Product description", example = "Keyboard for automation engineers") String description,

        @Schema(description = "Product price", example = "85.90") BigDecimal price,

        @Schema(description = "Available product stock", example = "20") Integer stock,

        @Schema(description = "Indicates whether the product is active", example = "true") Boolean active,

        @Schema(description = "Category ID associated with the product", example = "1") Long categoryId,

        @Schema(description = "Category name associated with the product", example = "Electronics") String categoryName) {

    /*
     * Converts the internal Product model into an HTTP response DTO.
     *
     * This keeps mapping logic close to the response contract and avoids
     * exposing the internal model directly to API consumers.
     */
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.stock(),
                product.active(),
                product.categoryId(),
                product.categoryName());
    }
}