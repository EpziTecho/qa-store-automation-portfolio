package com.qastore.api.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/*
 * ============================================================
 * File: ProductControllerIntegrationTest.java
 * Module: Product Tests
 *
 * Responsibility:
 * Validates ProductController endpoints using the real Spring application context.
 *
 * Interaction:
 * Uses MockMvc to execute HTTP requests against the application without starting
 * a real web server.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates controller-service integration.
 * - Ensures HTTP status codes and JSON contracts are correct.
 * - Provides regression protection for product endpoints.
 * ============================================================
 */

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/products should return product list")
    void shouldReturnProductList() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Laptop QA Pro")));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return product when product exists")
    void shouldReturnProductById() throws Exception {
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop QA Pro")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("GET /api/products/{id} should return 404 when product does not exist")
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Product with id 999 was not found")));
    }

    @Test
    @DisplayName("POST /api/products should create product when request is valid")
    void shouldCreateProductWhenRequestIsValid() throws Exception {
        String requestBody = """
                {
                  "name": "Mechanical Keyboard",
                  "description": "Keyboard for automation engineers",
                  "price": 85.90,
                  "stock": 20
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/products/3"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Mechanical Keyboard")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("POST /api/products should return 400 when request is invalid")
    void shouldReturnBadRequestWhenCreateProductRequestIsInvalid() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "description": "",
                  "price": -10,
                  "stock": -1
                }
                """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Request validation failed")));
    }
}