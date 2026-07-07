package com.qastore.api.health;

import io.swagger.v3.oas.annotations.media.Schema;

/*
 * ============================================================
 * File: HealthResponse.java
 * Module: Health
 *
 * Responsibility:
 * Represents the JSON response returned by the health endpoint.
 *
 * Interaction:
 * HealthController returns this DTO when GET /api/health is called.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - API contract clarity.
 * - Immutability through Java records.
 * - Separation between controller logic and response structure.
 * ============================================================
 */

@Schema(description = "Health status response for the QA Store API")
public record HealthResponse(

                @Schema(description = "Current service status", example = "UP") String status,

                @Schema(description = "Service name", example = "qa-store-api") String service,

                @Schema(description = "Service version", example = "1.0.0") String version,

                @Schema(description = "Human-readable health message", example = "QA Store API is running") String message) {
}