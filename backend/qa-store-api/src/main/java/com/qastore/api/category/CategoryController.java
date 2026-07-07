package com.qastore.api.category;

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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Categories", description = "Operations for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    /*
     * Constructor injection makes dependencies explicit and improves testability.
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Get all active categories", description = "Returns all active categories. Categories marked as inactive through soft delete are not returned.")
    @ApiResponse(responseCode = "200", description = "Active categories returned successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))))
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> response = categoryService.findAll()
                .stream()
                .map(CategoryResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Returns a single active category by its ID. If the category does not exist or is inactive, the API returns 404.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CategoryResponse> findById(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        Category category = categoryService.findById(id);

        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Creates a new product category. Category names must be unique.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Category name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        Category createdCategory = categoryService.create(request);

        URI location = URI.create("/api/categories/" + createdCategory.id());

        return ResponseEntity
                .created(location)
                .body(CategoryResponse.from(createdCategory));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing active category. The new category name must not be used by another category.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Category name already exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CategoryResponse> update(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id,

            @Valid @RequestBody UpdateCategoryRequest request) {
        Category updatedCategory = categoryService.update(id, request);

        return ResponseEntity.ok(CategoryResponse.from(updatedCategory));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete category", description = "Performs logical deletion by setting active = false. Categories with active products cannot be deleted.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted logically"),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Category has active products and cannot be deleted", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Category ID", example = "1") @PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}