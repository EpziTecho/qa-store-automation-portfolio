package com.qastore.api.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/*
 * ============================================================
 * File: LoginRequest.java
 * Module: Authentication
 *
 * Responsibility:
 * Represents the JSON request body required to authenticate a user.
 *
 * Interaction:
 * AuthController receives this DTO from HTTP POST /api/auth/login.
 * Bean Validation validates the fields before AuthService executes.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Separation of concerns: external login input is separated from user persistence.
 * - Fail fast: invalid requests are rejected before reaching authentication logic.
 * - Security: password is received only for validation and is never stored here.
 * ============================================================
 */

@Schema(description = "Request body used to authenticate a user")
public record LoginRequest(

        @Schema(description = "User email", example = "admin@qastore.com") @NotBlank(message = "Email is required") @Email(message = "Email must have a valid format") String email,

        @Schema(description = "User password", example = "Admin12345") @NotBlank(message = "Password is required") String password) {
}