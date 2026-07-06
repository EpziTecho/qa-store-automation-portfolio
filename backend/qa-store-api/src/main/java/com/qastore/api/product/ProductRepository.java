package com.qastore.api.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
 * ============================================================
 */

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    /*
     * Spring Data JPA derives this query automatically from the method name.
     *
     * Equivalent SQL concept:
     * SELECT * FROM products ORDER BY id ASC;
     */
    List<ProductEntity> findAllByOrderByIdAsc();
}