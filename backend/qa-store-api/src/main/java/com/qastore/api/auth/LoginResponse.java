package com.qastore.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/*
 * ============================================================
 * File: LoginResponse.java
 * Module: Authentication
 *
 * Responsibility:
 * Represents the JSON response returned after a successful login validation.
 *
 * Interaction:
 * AuthService creates this response after validating user credentials.
 * AuthController returns it to the API client.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - API contract clarity.
 * - Separation between authentication result and persistence entities.
 * - Security readiness: this DTO will later include JWT token information.
 * ============================================================
 */

@Schema(description = "Response returned after successful authentication")
public record LoginResponse(

        @Schema(description = "Indicates whether authentication succeeded", example = "true") Boolean authenticated,

        @Schema(description = "Authenticated user ID", example = "1") Long userId,

        @Schema(description = "Authenticated user email", example = "admin@qastore.com") String email,

        @Schema(description = "Roles assigned to the authenticated user", example = "[\"ROLE_ADMIN\"]") List<String> roles,

        @Schema(description = "Human-readable authentication message", example = "Login successful") String message) {
}