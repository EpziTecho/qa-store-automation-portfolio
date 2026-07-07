package com.qastore.api.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
 * ============================================================
 * File: RoleDataSeeder.java
 * Module: User Security Seed Data
 *
 * Responsibility:
 * Inserts default application roles when they do not exist.
 *
 * Interaction:
 * Runs during Spring Boot startup before UserDataSeeder.
 * Uses RoleRepository to persist ROLE_ADMIN and ROLE_USER in MySQL.
 *
 * Design Pattern:
 * Application Data Seeder.
 *
 * Engineering Principles:
 * - Idempotency: roles are created only if they do not already exist.
 * - Developer experience: provides required security data automatically.
 * - Predictability: authentication tests can rely on known roles.
 * ============================================================
 */

@Component
@Order(3)
public class RoleDataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfMissing(
                RoleName.ROLE_ADMIN,
                "Administrator role with full catalog management permissions");

        createRoleIfMissing(
                RoleName.ROLE_USER,
                "Standard user role with read-oriented permissions");
    }

    private void createRoleIfMissing(RoleName roleName, String description) {
        if (roleRepository.existsByName(roleName)) {
            return;
        }

        roleRepository.save(new RoleEntity(roleName, description));
    }
}