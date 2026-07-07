package com.qastore.api.category;

/*
 * ============================================================
 * File: CategoryMapper.java
 * Module: Category Mapping
 *
 * Responsibility:
 * Converts between persistence entities and domain models.
 *
 * Interaction:
 * CategoryJpaService uses this mapper to keep JPA entities isolated from
 * controller-level response contracts.
 *
 * Design Pattern:
 * Mapper Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: mapping logic is isolated.
 * - Clean architecture mindset: controllers do not expose JPA entities.
 * - Maintainability: future field changes are centralized here.
 * ============================================================
 */

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getActive());
    }

    public static CategoryEntity toEntity(CreateCategoryRequest request) {
        return new CategoryEntity(
                request.name(),
                request.description(),
                true);
    }
}