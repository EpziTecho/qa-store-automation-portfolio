package com.qastore.api.health;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * ============================================================
 * File: HealthControllerTest.java
 * Module: Health Check Tests
 *
 * Responsibility:
 * Validates the behavior of the HealthController endpoint.
 *
 * Interaction:
 * Uses Spring MockMvc to simulate HTTP requests without starting a real server.
 *
 * Design Pattern:
 * Test Slice pattern using @WebMvcTest.
 *
 * Engineering Principles:
 * - Fast feedback: tests only the web layer.
 * - Isolation: avoids loading unnecessary application components.
 * - Testability: validates HTTP contract, status code and JSON body.
 * ============================================================
 */

/**
 * Test class for HealthController.
 *
 * @WebMvcTest loads only the Spring MVC components required to test the
 *             controller.
 *             This is faster than loading the entire application context.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/health should return API health information")
    void shouldReturnApiHealthInformation() throws Exception {

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("qa-store-api")))
                .andExpect(jsonPath("$.version", is("1.0.0")))
                .andExpect(jsonPath("$.message", is("QA Store API is running")));
    }
}