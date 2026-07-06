package com.qastore.api.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * ============================================================
 * File: CategoryRepository.java
 * Module: Category Persistence
 *
 * Responsibility:
 * Provides database access operations for CategoryEntity.
 *
 * Interaction:
 * CategoryJpaService and ProductJpaService use this repository to query
 * categories from MySQL.
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

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /*
     * Returns all active categories ordered by id.
     *
     * This supports soft delete because inactive categories should not appear
     * in normal API responses.
     */
    List<CategoryEntity> findAllByActiveTrueOrderByIdAsc();

    /*
     * Finds an active category by id.
     *
     * If the category exists but active = false, the API treats it as not found.
     */
    Optional<CategoryEntity> findByIdAndActiveTrue(Long id);

    /*
     * Checks whether a category name already exists regardless of casing.
     *
     * Used during category creation.
     */
    boolean existsByNameIgnoreCase(String name);

    /*
     * Checks whether another category already uses the same name.
     *
     * Used during update to prevent changing a category name to a duplicated one.
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    /*
     * Finds a category by name ignoring uppercase/lowercase differences.
     *
     * Used by seeders and business logic where category names must be treated
     * consistently regardless of text casing.
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
}