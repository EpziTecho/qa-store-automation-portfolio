package com.qastore.api.product;

/*
 * ============================================================
 * File: ProductMapper.java
 * Module: Product Mapping
 *
 * Responsibility:
 * Converts between persistence entities and domain models.
 *
 * Interaction:
 * ProductJpaService uses this mapper to avoid exposing JPA entities outside
 * the persistence-focused implementation.
 *
 * Design Pattern:
 * Mapper Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: mapping is isolated from business operations.
 * - Clean architecture mindset: controllers do not know about JPA entities.
 * - Maintainability: future field changes are centralized here.
 * ============================================================
 */

public final class ProductMapper {

    /*
     * Private constructor prevents instantiation because this class only
     * provides static mapping methods.
     */
    private ProductMapper() {
    }

    public static Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getActive());
    }

    public static ProductEntity toEntity(CreateProductRequest request) {
        return new ProductEntity(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                true);
    }
}