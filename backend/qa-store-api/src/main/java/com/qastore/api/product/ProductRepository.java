package com.qastore.api.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * ============================================================
 * File: ProductRepository.java
 * Module: Product Persistence
 *
 * Responsibility:
 * Provides database access operations for ProductEntity.
 *
 * Interaction:
 * ProductJpaService uses this repository to query and persist products.
 * Spring Data JPA generates the implementation automatically at runtime.
 *
 * Design Pattern:
 * Repository Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: persistence access is isolated in a repository.
 * - DRY: avoids writing boilerplate SQL for common CRUD operations.
 * - Abstraction: service layer does not directly interact with EntityManager.
 * - Soft delete support: queries can filter active records only.
 * ============================================================
 */

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    /*
     * Returns only active products ordered by id.
     *
     * This supports soft delete because inactive products should not appear
     * in normal API responses.
     */
    List<ProductEntity> findAllByActiveTrueOrderByIdAsc();

    /*
     * Finds an active product by id.
     *
     * If the product exists but active = false, the API will treat it as not found.
     */
    Optional<ProductEntity> findByIdAndActiveTrue(Long id);

    /*
     * Used later by category business rules to verify whether a category
     * has active products assigned.
     */
    boolean existsByCategoryIdAndActiveTrue(Long categoryId);
}