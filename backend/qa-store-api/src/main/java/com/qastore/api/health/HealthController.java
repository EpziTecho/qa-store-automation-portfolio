package com.qastore.api.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * ============================================================
 * File: HealthController.java
 * Module: Health
 *
 * Responsibility:
 * Exposes a lightweight endpoint to verify that the API is running.
 *
 * Interaction:
 * It does not depend on database access or business services.
 * It is used by developers, QA engineers, monitoring tools and CI/CD pipelines.
 *
 * Design Pattern:
 * MVC Controller pattern.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: only exposes API health status.
 * - Operational readiness: useful for smoke tests and deployment validation.
 * - Low coupling: does not depend on database or external services.
 * ============================================================
 */

@RestController
@Tag(name = "Health", description = "Endpoint used to verify that the QA Store API is running")
public class HealthController {

    @GetMapping("/api/health")
    @Operation(summary = "Check API health", description = "Returns a simple health response confirming that the QA Store API is running.")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse(
                "UP",
                "qa-store-api",
                "1.0.0",
                "QA Store API is running");

        return ResponseEntity.ok(response);
    }
}