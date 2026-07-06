package com.qastore.api.product;

import java.math.BigDecimal;

/*
 * ============================================================
 * File: Product.java
 * Module: Product
 *
 * Responsibility:
 * Represents the internal product domain model used by the service layer.
 *
 * Interaction:
 * ProductService works with this model internally and ProductController
 * converts it into ProductResponse objects for HTTP responses.
 *
 * Design Pattern:
 * Domain Model.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: represents product data only.
 * - Immutability: Java records are immutable by default.
 * - Separation of concerns: internal model is separated from API DTOs.
 * ============================================================
 */

public record Product(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        Boolean active) {
}