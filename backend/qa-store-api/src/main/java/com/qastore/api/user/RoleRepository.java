package com.qastore.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * ============================================================
 * File: RoleRepository.java
 * Module: User Security Persistence
 *
 * Responsibility:
 * Provides database access operations for RoleEntity.
 *
 * Interaction:
 * RoleDataSeeder uses this repository to create default roles.
 * UserDataSeeder uses it to assign roles to default users.
 * Future authorization services will use it to resolve role assignments.
 *
 * Design Pattern:
 * Repository Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: persistence access is isolated.
 * - DRY: Spring Data JPA generates common CRUD operations.
 * - Type safety: role lookup uses RoleName enum.
 * ============================================================
 */

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(RoleName name);

    boolean existsByName(RoleName name);
}