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

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> response = categoryService.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        Category createdCategory = categoryService.create(request);

        URI location = URI.create("/api/categories/" + createdCategory.id());

        return ResponseEntity
                .created(location)
                .body(CategoryResponse.from(createdCategory));
    }
}