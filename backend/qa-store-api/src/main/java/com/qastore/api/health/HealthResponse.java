package com.qastore.api.health;

/*
 * ============================================================
 * File: HealthResponse.java
 * Module: Health Check
 *
 * Responsibility:
 * Represents the response body returned by the health check endpoint.
 *
 * Interaction:
 * This record is created by HealthController and serialized by Spring Boot
 * into JSON when the client calls GET /api/health.
 *
 * Design Pattern:
 * DTO (Data Transfer Object).
 *
 * Engineering Principles:
 * - Single Responsibility Principle: this class only represents response data.
 * - Immutability: Java records are immutable by default.
 * - Clean Code: the response contract is explicit and easy to understand.
 * ============================================================
 */

/**
 * HealthResponse defines the JSON contract returned by the health endpoint.
 *
 * Java records are useful for simple immutable DTOs because they automatically
 * provide constructor, getters, equals, hashCode and toString.
 */
public record HealthResponse(
        String status,
        String service,
        String version,
        String message
) {
}