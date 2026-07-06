package com.qastore.api.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

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
 * a real web server. The test persists data using ProductRepository.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates controller-service-repository integration.
 * - Avoids hardcoded database IDs.
 * - Ensures HTTP status codes and JSON contracts are correct.
 * - Provides regression protection for product endpoints.
 * ============================================================
 */

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProductRepository productRepository;

        /*
         * Cleans product data before each test.
         *
         * This avoids tests depending on records created by previous executions.
         * The database auto-increment value may continue increasing, so tests must
         * never assume fixed IDs.
         */
        @BeforeEach
        void setUp() {
                productRepository.deleteAll();
        }

        @Test
        @DisplayName("GET /api/products should return product list")
        void shouldReturnProductList() throws Exception {
                productRepository.save(new ProductEntity(
                                "Laptop QA Pro",
                                "Laptop designed for automation testing practice",
                                new BigDecimal("1200.00"),
                                10,
                                true));

                productRepository.save(new ProductEntity(
                                "Wireless Mouse",
                                "Wireless mouse for QA workstation",
                                new BigDecimal("25.50"),
                                50,
                                true));

                mockMvc.perform(get("/api/products"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                                .andExpect(jsonPath("$[0].name", is("Laptop QA Pro")))
                                .andExpect(jsonPath("$[0].active", is(true)));
        }

        @Test
        @DisplayName("GET /api/products/{id} should return product when product exists")
        void shouldReturnProductById() throws Exception {
                ProductEntity product = productRepository.save(new ProductEntity(
                                "Laptop QA Pro",
                                "Laptop designed for automation testing practice",
                                new BigDecimal("1200.00"),
                                10,
                                true));

                mockMvc.perform(get("/api/products/" + product.getId()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$.name", is("Laptop QA Pro")))
                                .andExpect(jsonPath("$.active", is(true)));
        }

        @Test
        @DisplayName("GET /api/products/{id} should return 404 when product does not exist")
        void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
                mockMvc.perform(get("/api/products/999999"))
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.error", is("Not Found")))
                                .andExpect(jsonPath("$.message", is("Product with id 999999 was not found")));
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
                                .andExpect(header().string("Location", startsWith("/api/products/")))
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.name", is("Mechanical Keyboard")))
                                .andExpect(jsonPath("$.description", is("Keyboard for automation engineers")))
                                .andExpect(jsonPath("$.stock", is(20)))
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