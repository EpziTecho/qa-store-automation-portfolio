package com.qastore.api.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * ============================================================
 * File: CategoryRepository.java
 * Module: Category Persistence
 *
 * Responsibility:
 * Provides database access operations for CategoryEntity.
 *
 * Interaction:
 * CategoryJpaService uses this repository to query and persist categories.
 * Spring Data JPA generates the implementation automatically at runtime.
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
}