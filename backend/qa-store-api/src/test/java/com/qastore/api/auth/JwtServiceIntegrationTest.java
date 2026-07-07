package com.qastore.api.auth;

import com.qastore.api.user.RoleEntity;
import com.qastore.api.user.RoleName;
import com.qastore.api.user.UserEntity;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import org.springframework.security.core.userdetails.User;
/*
 * ============================================================
 * File: JwtServiceIntegrationTest.java
 * Module: Authentication Tests
 *
 * Responsibility:
 * Validates JWT generation and claim extraction.
 *
 * Interaction:
 * Uses the real JwtService bean from the Spring application context.
 * Creates an in-memory UserEntity object only to generate a token.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates security token generation.
 * - Validates token subject and custom claims.
 * - Provides regression protection before JWT filter implementation.
 * ============================================================
 */

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtServiceIntegrationTest {

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Should generate JWT and extract expected claims")
    void shouldGenerateJwtAndExtractExpectedClaims() {
        RoleEntity adminRole = new RoleEntity(
                RoleName.ROLE_ADMIN,
                "Administrator role");

        UserEntity user = new UserEntity(
                "System",
                "Administrator",
                "admin@qastore.com",
                "alreadyEncodedPassword",
                true);

        user.addRole(adminRole);

        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractClaims(token);

        assertThat(token).isNotBlank();
        assertThat(claims.getSubject()).isEqualTo("admin@qastore.com");
        assertThat(claims.get("roles", List.class)).contains("ROLE_ADMIN");

        User userDetails = new User(
                "admin@qastore.com",
                "alreadyEncodedPassword",
                List.of(() -> "ROLE_ADMIN"));

        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();

    }
}