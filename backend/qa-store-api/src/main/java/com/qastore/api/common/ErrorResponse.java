package com.qastore.api.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/*
 * ============================================================
 * File: ErrorResponse.java
 * Module: Common Error Handling
 *
 * Responsibility:
 * Represents the standard JSON error response returned by the API.
 *
 * Interaction:
 * GlobalExceptionHandler creates this DTO whenever a controlled exception
 * must be converted into an HTTP error response.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Consistent error contract.
 * - Separation of concerns: error formatting is centralized.
 * - API clarity: clients receive predictable error structures.
 * ============================================================
 */

@Schema(description = "Standard error response returned by the API")
public record ErrorResponse(

                @Schema(description = "Timestamp when the error occurred", example = "2026-07-06T18:30:00") LocalDateTime timestamp,

                @Schema(description = "HTTP status code", example = "404") int status,

                @Schema(description = "HTTP status reason phrase", example = "Not Found") String error,

                @Schema(description = "Human-readable error message", example = "Product with id 999999 was not found") String message,

                @Schema(description = "Request path that produced the error", example = "/api/products/999999") String path,

                @Schema(description = "Detailed validation or business errors") List<String> details) {
}