package com.qastore.api.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * ============================================================
 * File: HealthController.java
 * Module: Health Check
 *
 * Responsibility:
 * Exposes a REST endpoint to verify that the QA Store API is running.
 *
 * Interaction:
 * Receives HTTP requests at /api/health and returns a HealthResponse object.
 * Spring Boot automatically serializes the response into JSON.
 *
 * Design Pattern:
 * MVC Controller pattern.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: this controller only handles health checks.
 * - Separation of Concerns: HTTP handling is isolated from future business logic.
 * - KISS: the endpoint is intentionally simple because it only validates service availability.
 * ============================================================
 */

/**
 * REST controller responsible for exposing technical health information.
 *
 * This endpoint will be useful for:
 * - Local backend validation.
 * - Postman smoke testing.
 * - Docker container checks.
 * - GitHub Actions pipeline validation.
 * - Future monitoring integrations.
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * Handles GET /api/health requests.
     *
     * ResponseEntity is used to explicitly control the HTTP response.
     * In this case, the endpoint returns HTTP 200 OK with a stable JSON body.
     *
     * @return HTTP 200 response containing the API health status.
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {

        HealthResponse response = new HealthResponse(
                "UP",
                "qa-store-api",
                "1.0.0",
                "QA Store API is running"
        );

        return ResponseEntity.ok(response);
    }
}