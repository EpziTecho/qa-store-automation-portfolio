package com.qastore.api.product;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/*
 * ============================================================
 * File: ProductController.java
 * Module: Product
 *
 * Responsibility:
 * Exposes REST endpoints for product operations.
 *
 * Interaction:
 * Receives HTTP requests, delegates business operations to ProductService
 * and returns ProductResponse DTOs.
 *
 * Design Pattern:
 * MVC Controller pattern.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: handles only product HTTP operations.
 * - Separation of concerns: business logic is delegated to ProductService.
 * - Dependency Inversion: depends on ProductService abstraction.
 * - RESTful API design: uses HTTP verbs and status codes properly.
 * ============================================================
 */

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /*
     * Constructor injection is preferred over field injection because:
     * - Dependencies are explicit.
     * - The class is easier to test.
     * - Required dependencies cannot be forgotten.
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*
     * GET /api/products
     *
     * Returns all available products.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> response = productService.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    /*
     * GET /api/products/{id}
     *
     * Returns a single product by id.
     * If the product does not exist, ProductNotFoundException is thrown
     * and handled by GlobalExceptionHandler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.ok(ProductResponse.from(product));
    }

    /*
     * POST /api/products
     *
     * Creates a new product.
     * 
     * @Valid triggers Bean Validation before the method body executes.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product createdProduct = productService.create(request);

        URI location = URI.create("/api/products/" + createdProduct.id());

        return ResponseEntity
                .created(location)
                .body(ProductResponse.from(createdProduct));
    }
}