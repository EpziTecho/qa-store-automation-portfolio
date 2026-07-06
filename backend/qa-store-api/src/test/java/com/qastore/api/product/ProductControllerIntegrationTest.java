package com.qastore.api.product;

import com.qastore.api.category.CategoryEntity;
import com.qastore.api.category.CategoryRepository;
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
 * a real web server. The test persists data using ProductRepository and
 * CategoryRepository.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates controller-service-repository integration.
 * - Avoids hardcoded database IDs.
 * - Validates Product-Category relationship.
 * - Ensures HTTP status codes and JSON contracts are correct.
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

        @Autowired
        private CategoryRepository categoryRepository;

        @BeforeEach
        void setUp() {
                productRepository.deleteAll();
                categoryRepository.deleteAll();
        }

        @Test
        @DisplayName("GET /api/products should return product list with category data")
        void shouldReturnProductList() throws Exception {
                CategoryEntity electronics = categoryRepository.save(new CategoryEntity(
                                "Electronics",
                                "Electronic devices and accessories",
                                true));

                productRepository.save(new ProductEntity(
                                "Laptop QA Pro",
                                "Laptop designed for automation testing practice",
                                new BigDecimal("1200.00"),
                                10,
                                true,
                                electronics));

                productRepository.save(new ProductEntity(
                                "Monitor QA View",
                                "Monitor for QA workstation",
                                new BigDecimal("300.00"),
                                15,
                                true,
                                electronics));

                mockMvc.perform(get("/api/products"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                                .andExpect(jsonPath("$[0].name", is("Laptop QA Pro")))
                                .andExpect(jsonPath("$[0].active", is(true)))
                                .andExpect(jsonPath("$[0].categoryId", is(electronics.getId().intValue())))
                                .andExpect(jsonPath("$[0].categoryName", is("Electronics")));
        }

        @Test
        @DisplayName("GET /api/products/{id} should return product when product exists")
        void shouldReturnProductById() throws Exception {
                CategoryEntity electronics = categoryRepository.save(new CategoryEntity(
                                "Electronics",
                                "Electronic devices and accessories",
                                true));

                ProductEntity product = productRepository.save(new ProductEntity(
                                "Laptop QA Pro",
                                "Laptop designed for automation testing practice",
                                new BigDecimal("1200.00"),
                                10,
                                true,
                                electronics));

                mockMvc.perform(get("/api/products/" + product.getId()))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                                .andExpect(jsonPath("$.name", is("Laptop QA Pro")))
                                .andExpect(jsonPath("$.active", is(true)))
                                .andExpect(jsonPath("$.categoryId", is(electronics.getId().intValue())))
                                .andExpect(jsonPath("$.categoryName", is("Electronics")));
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
                CategoryEntity accessories = categoryRepository.save(new CategoryEntity(
                                "Accessories",
                                "General workstation accessories",
                                true));

                String requestBody = """
                                {
                                  "name": "Mechanical Keyboard",
                                  "description": "Keyboard for automation engineers",
                                  "price": 85.90,
                                  "stock": 20,
                                  "categoryId": %d
                                }
                                """.formatted(accessories.getId());

                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", startsWith("/api/products/")))
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.name", is("Mechanical Keyboard")))
                                .andExpect(jsonPath("$.description", is("Keyboard for automation engineers")))
                                .andExpect(jsonPath("$.stock", is(20)))
                                .andExpect(jsonPath("$.active", is(true)))
                                .andExpect(jsonPath("$.categoryId", is(accessories.getId().intValue())))
                                .andExpect(jsonPath("$.categoryName", is("Accessories")));
        }

        @Test
        @DisplayName("POST /api/products should return 404 when category does not exist")
        void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
                String requestBody = """
                                {
                                  "name": "Mechanical Keyboard",
                                  "description": "Keyboard for automation engineers",
                                  "price": 85.90,
                                  "stock": 20,
                                  "categoryId": 999999
                                }
                                """;

                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                                .andExpect(status().isNotFound())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.status", is(404)))
                                .andExpect(jsonPath("$.error", is("Not Found")))
                                .andExpect(jsonPath("$.message", is("Category with id 999999 was not found")));
        }

        @Test
        @DisplayName("POST /api/products should return 400 when request is invalid")
        void shouldReturnBadRequestWhenCreateProductRequestIsInvalid() throws Exception {
                String requestBody = """
                                {
                                  "name": "",
                                  "description": "",
                                  "price": -10,
                                  "stock": -1,
                                  "categoryId": null
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