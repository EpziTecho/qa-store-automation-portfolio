package com.qastore.api.product;

/*
 * ============================================================
 * File: ProductNotFoundException.java
 * Module: Product
 *
 * Responsibility:
 * Represents the business error thrown when a product does not exist.
 *
 * Interaction:
 * InMemoryProductService throws this exception.
 * GlobalExceptionHandler converts it into an HTTP 404 response.
 *
 * Design Pattern:
 * Domain-specific Exception.
 *
 * Engineering Principles:
 * - Explicit error modeling.
 * - Separation of concerns: service throws business error, web layer maps it.
 * - Clean Code: avoids returning null when a product is not found.
 * ============================================================
 */

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " was not found");
    }
}