package com.qastore.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/*
 * ============================================================
 * File: CurrentUserResponse.java
 * Module: Authentication
 *
 * Responsibility:
 * Represents the authenticated user returned by GET /api/auth/me.
 *
 * Interaction:
 * AuthController builds this response from the authenticated principal stored
 * in Spring Security's SecurityContext.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - API contract clarity.
 * - Security: exposes identity and roles but never exposes password data.
 * - Separation of concerns: response model is isolated from UserEntity.
 * ============================================================
 */

@Schema(description = "Authenticated user information")
public record CurrentUserResponse(

        @Schema(description = "Authenticated user ID", example = "1") Long userId,

        @Schema(description = "Authenticated user email", example = "admin@qastore.com") String email,

        @Schema(description = "Assigned user roles", example = "[\"ROLE_ADMIN\"]") List<String> roles) {
}