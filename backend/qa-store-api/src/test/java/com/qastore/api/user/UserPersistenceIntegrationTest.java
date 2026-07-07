package com.qastore.api.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * ============================================================
 * File: UserPersistenceIntegrationTest.java
 * Module: User Security Tests
 *
 * Responsibility:
 * Validates that users and roles can be persisted and queried using MySQL.
 *
 * Interaction:
 * Uses UserRepository and RoleRepository with the real Spring application context.
 * Uses PasswordEncoder to validate that passwords are stored as hashes.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates persistence mapping.
 * - Validates many-to-many relationship between users and roles.
 * - Validates password hashing behavior.
 * - Avoids storing plain text passwords.
 * ============================================================
 */

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserPersistenceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Should persist user with admin role and encoded password")
    void shouldPersistUserWithAdminRoleAndEncodedPassword() {
        RoleEntity adminRole = roleRepository.save(new RoleEntity(
                RoleName.ROLE_ADMIN,
                "Administrator role"));

        String rawPassword = "Admin12345";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity(
                "System",
                "Administrator",
                "admin@qastore.com",
                encodedPassword,
                true);

        user.addRole(adminRole);

        UserEntity savedUser = userRepository.save(user);

        UserEntity foundUser = userRepository.findByEmailIgnoreCase("ADMIN@QASTORE.COM")
                .orElseThrow();

        assertThat(savedUser.getId()).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("admin@qastore.com");
        assertThat(foundUser.getPasswordHash()).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, foundUser.getPasswordHash())).isTrue();
        assertThat(foundUser.getRoles()).hasSize(1);
        assertThat(foundUser.getRoles().iterator().next().getName()).isEqualTo(RoleName.ROLE_ADMIN);
    }

    @Test
    @DisplayName("Should verify whether user email already exists ignoring case")
    void shouldVerifyWhetherUserEmailExistsIgnoringCase() {
        RoleEntity userRole = roleRepository.save(new RoleEntity(
                RoleName.ROLE_USER,
                "Standard user role"));

        UserEntity user = new UserEntity(
                "Regular",
                "User",
                "user@qastore.com",
                passwordEncoder.encode("User12345"),
                true);

        user.addRole(userRole);

        userRepository.save(user);

        boolean exists = userRepository.existsByEmailIgnoreCase("USER@QASTORE.COM");

        assertThat(exists).isTrue();
    }
}