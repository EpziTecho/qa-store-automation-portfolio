package com.qastore.api.product;

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

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Boolean active) {

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
                product.active());
    }
}