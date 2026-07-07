package com.qastore.api.product;

import com.qastore.api.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
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
@Tag(name = "Products", description = "Operations for managing products in the QA Store catalog")
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

    @GetMapping
    @Operation(summary = "Get all active products", description = "Returns all products that are currently active. Products marked as inactive through soft delete are not returned.")
    @ApiResponse(responseCode = "200", description = "Active products returned successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))))
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> response = productService.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a single active product by its ID. If the product does not exist or is inactive, the API returns 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> findById(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        Product product = productService.findById(id);

        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @PostMapping
    @Operation(summary = "Create product", description = "Creates a new product associated with an existing category.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product createdProduct = productService.create(request);

        URI location = URI.create("/api/products/" + createdProduct.id());

        return ResponseEntity
                .created(location)
                .body(ProductResponse.from(createdProduct));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Updates an existing active product. The product can also be moved to another existing category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product or category not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProductResponse> update(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id,

            @Valid @RequestBody UpdateProductRequest request) {
        Product updatedProduct = productService.update(id, request);

        return ResponseEntity.ok(ProductResponse.from(updatedProduct));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete product", description = "Performs logical deletion by setting active = false. The product remains in the database but is hidden from normal API queries.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted logically"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Product ID", example = "1") @PathVariable Long id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }
}