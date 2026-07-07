package com.qastore.api.auth;

/*
 * ============================================================
 * File: InvalidCredentialsException.java
 * Module: Authentication
 *
 * Responsibility:
 * Represents an authentication failure caused by invalid credentials
 * or an inactive user account.
 *
 * Interaction:
 * AuthService throws this exception.
 * GlobalExceptionHandler converts it into an HTTP 401 Unauthorized response.
 *
 * Design Pattern:
 * Domain-specific Exception.
 *
 * Engineering Principles:
 * - Explicit error modeling.
 * - Security: uses a generic message to avoid revealing whether an email exists.
 * - Separation of concerns: service throws business/security error, web layer maps it.
 * ============================================================
 */

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}