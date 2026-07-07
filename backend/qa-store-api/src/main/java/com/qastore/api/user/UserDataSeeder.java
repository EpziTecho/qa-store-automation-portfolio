package com.qastore.api.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*
 * ============================================================
 * File: UserDataSeeder.java
 * Module: User Security Seed Data
 *
 * Responsibility:
 * Inserts a default administrator user when it does not exist.
 *
 * Interaction:
 * Runs during Spring Boot startup after RoleDataSeeder.
 * Uses UserRepository to persist the admin user.
 * Uses RoleRepository to assign ROLE_ADMIN.
 * Uses PasswordEncoder to hash the default password.
 *
 * Design Pattern:
 * Application Data Seeder.
 *
 * Engineering Principles:
 * - Security: stores password hash instead of plain text password.
 * - Idempotency: creates the admin user only once.
 * - Developer experience: provides a known user for local authentication testing.
 * ============================================================
 */

@Component
@Order(4)
public class UserDataSeeder implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_EMAIL = "admin@qastore.com";
    private static final String DEFAULT_ADMIN_PASSWORD = "Admin12345";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataSeeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmailIgnoreCase(DEFAULT_ADMIN_EMAIL)) {
            return;
        }

        RoleEntity adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN was not found"));

        UserEntity adminUser = new UserEntity(
                "System",
                "Administrator",
                DEFAULT_ADMIN_EMAIL,
                passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD),
                true);

        adminUser.addRole(adminRole);

        userRepository.save(adminUser);
    }
}