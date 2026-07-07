package com.qastore.api.user;

/*
 * ============================================================
 * File: RoleName.java
 * Module: User Security
 *
 * Responsibility:
 * Defines the supported application roles.
 *
 * Interaction:
 * RoleEntity stores one RoleName value.
 * UserEntity can be associated with one or more roles.
 * Future authorization rules will use these role names to protect endpoints.
 *
 * Design Pattern:
 * Enumeration.
 *
 * Engineering Principles:
 * - Type safety: avoids using arbitrary role strings across the codebase.
 * - Centralization: supported roles are defined in one place.
 * - Maintainability: adding new roles becomes explicit.
 * ============================================================
 */

public enum RoleName {

    ROLE_ADMIN,
    ROLE_USER
}