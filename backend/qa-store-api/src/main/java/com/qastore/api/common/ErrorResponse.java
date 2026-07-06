package com.qastore.api.common;

import java.time.LocalDateTime;
import java.util.List;

/*
 * ============================================================
 * File: ErrorResponse.java
 * Module: Common Error Handling
 *
 * Responsibility:
 * Represents the standard error response returned by the API.
 *
 * Interaction:
 * GlobalExceptionHandler creates this DTO when exceptions occur.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Consistent API error contract.
 * - Separation of error representation from business logic.
 * - Immutability through Java records.
 * ============================================================
 */

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details) {
}