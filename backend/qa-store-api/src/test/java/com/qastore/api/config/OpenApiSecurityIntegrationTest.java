package com.qastore.api.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/*
 * ============================================================
 * File: OpenApiSecurityIntegrationTest.java
 * Module: API Documentation Tests
 *
 * Responsibility:
 * Validates that the OpenAPI contract exposes the JWT Bearer security scheme.
 *
 * Interaction:
 * Uses MockMvc to request /v3/api-docs and verify that Swagger/OpenAPI contains
 * the bearerAuth scheme required by protected endpoints.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Contract validation: ensures API documentation includes security metadata.
 * - Regression protection: prevents accidentally removing JWT documentation.
 * - QA readiness: validates that Swagger can support authenticated API testing.
 * ============================================================
 */

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OpenAPI contract should expose Bearer JWT security scheme")
    void shouldExposeBearerJwtSecurityScheme() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.type", is("http")))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme", is("bearer")))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.bearerFormat", is("JWT")));
    }

    @Test
    @DisplayName("OpenAPI contract should mark protected auth endpoint with bearerAuth")
    void shouldMarkProtectedAuthEndpointWithBearerAuth() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/auth/me'].get.security[0].bearerAuth").exists());
    }
}