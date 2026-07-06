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
 * ============================================================
 */

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByOrderByIdAsc();

    boolean existsByNameIgnoreCase(String name);

    /*
     * Finds a category by name ignoring uppercase/lowercase differences.
     *
     * Used by seeders and business logic where category names must be treated
     * consistently regardless of text casing.
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
}