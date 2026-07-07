package com.qastore.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * ============================================================
 * File: UserRepository.java
 * Module: User Security Persistence
 *
 * Responsibility:
 * Provides database access operations for UserEntity.
 *
 * Interaction:
 * UserDataSeeder uses this repository to create a default admin user.
 * Future AuthService will use it to find users by email during login.
 *
 * Design Pattern:
 * Repository Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: user persistence is isolated.
 * - DRY: Spring Data JPA generates common database operations.
 * - Authentication readiness: includes email-based lookup.
 * ============================================================
 */

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}