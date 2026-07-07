package com.qastore.api.category;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/*
 * ============================================================
 * File: CategoryController.java
 * Module: Category
 *
 * Responsibility:
 * Exposes REST endpoints for category operations.
 *
 * Interaction:
 * Receives HTTP requests, delegates business operations to CategoryService
 * and returns CategoryResponse DTOs.
 *
 * Design Pattern:
 * MVC Controller pattern.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: handles only category HTTP operations.
 * - Separation of concerns: business logic is delegated to CategoryService.
 * - Dependency Inversion: depends on CategoryService abstraction.
 * - RESTful API design: uses HTTP verbs and status codes properly.
 * ============================================================
 */

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /*
     * Constructor injection makes dependencies explicit and improves testability.
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /*
     * GET /api/categories
     *
     * Returns all active categories.
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> response = categoryService.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    /*
     * GET /api/categories/{id}
     *
     * Returns a single active category by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    /*
     * POST /api/categories
     *
     * Creates a new category.
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        Category createdCategory = categoryService.create(request);

        URI location = URI.create("/api/categories/" + createdCategory.id());

        return ResponseEntity
                .created(location)
                .body(CategoryResponse.from(createdCategory));
    }

    /*
     * PUT /api/categories/{id}
     *
     * Updates an existing active category.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        Category updatedCategory = categoryService.update(id, request);

        return ResponseEntity.ok(CategoryResponse.from(updatedCategory));
    }

    /*
     * DELETE /api/categories/{id}
     *
     * Performs logical deletion only if the category has no active products.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}