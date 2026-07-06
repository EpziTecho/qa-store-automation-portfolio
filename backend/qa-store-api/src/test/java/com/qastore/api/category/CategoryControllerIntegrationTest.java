package com.qastore.api.category;

import com.qastore.api.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.qastore.api.product.ProductEntity;
import com.qastore.api.product.ProductRepository;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
/*
 * ============================================================
 * File: CategoryControllerIntegrationTest.java
 * Module: Category Tests
 *
 * Responsibility:
 * Validates CategoryController endpoints using the real Spring application context.
 *
 * Interaction:
 * Uses MockMvc to execute HTTP requests against the application without starting
 * a real web server. The test persists data using CategoryRepository.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates controller-service-repository integration.
 * - Avoids hardcoded database IDs.
 * - Ensures HTTP status codes and JSON contracts are correct.
 * - Provides regression protection for category endpoints.
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
class CategoryControllerIntegrationTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/categories should return category list")
    void shouldReturnCategoryList() throws Exception {
        categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        categoryRepository.save(new CategoryEntity(
                "Accessories",
                "General workstation accessories",
                true));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].name", is("Electronics")))
                .andExpect(jsonPath("$[0].active", is(true)));
    }

    @Test
    @DisplayName("GET /api/categories/{id} should return category when category exists")
    void shouldReturnCategoryById() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        mockMvc.perform(get("/api/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("GET /api/categories/{id} should return 404 when category does not exist")
    void shouldReturnNotFoundWhenCategoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/categories/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Category with id 999999 was not found")));
    }

    @Test
    @DisplayName("POST /api/categories should create category when request is valid")
    void shouldCreateCategoryWhenRequestIsValid() throws Exception {
        String requestBody = """
                {
                  "name": "Gaming",
                  "description": "Gaming products and accessories"
                }
                """;

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/categories/")))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Gaming")))
                .andExpect(jsonPath("$.description", is("Gaming products and accessories")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("POST /api/categories should return 400 when request is invalid")
    void shouldReturnBadRequestWhenCreateCategoryRequestIsInvalid() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "description": ""
                }
                """;

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Request validation failed")));
    }

    @Test
    @DisplayName("POST /api/categories should return 409 when category name already exists")
    void shouldReturnConflictWhenCategoryNameAlreadyExists() throws Exception {
        categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        String requestBody = """
                {
                  "name": "Electronics",
                  "description": "Duplicated category"
                }
                """;

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", is("Category with name 'Electronics' already exists")));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} should update category when request is valid")
    void shouldUpdateCategoryWhenRequestIsValid() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        String requestBody = """
                {
                  "name": "Updated Electronics",
                  "description": "Updated category description"
                }
                """;

        mockMvc.perform(put("/api/categories/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Updated Electronics")))
                .andExpect(jsonPath("$.description", is("Updated category description")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} should return 404 when category does not exist")
    void shouldReturnNotFoundWhenUpdatingCategoryDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "name": "Updated Electronics",
                  "description": "Updated category description"
                }
                """;

        mockMvc.perform(put("/api/categories/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Category with id 999999 was not found")));
    }

    @Test
    @DisplayName("PUT /api/categories/{id} should return 409 when category name already exists")
    void shouldReturnConflictWhenUpdatingCategoryWithDuplicatedName() throws Exception {
        categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        CategoryEntity accessories = categoryRepository.save(new CategoryEntity(
                "Accessories",
                "General workstation accessories",
                true));

        String requestBody = """
                {
                  "name": "Electronics",
                  "description": "Trying to duplicate category name"
                }
                """;

        mockMvc.perform(put("/api/categories/" + accessories.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("Category with name 'Electronics' already exists")));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} should deactivate category when category has no active products")
    void shouldDeactivateCategoryWhenCategoryHasNoActiveProducts() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(
                "Temporary",
                "Temporary category without products",
                true));

        mockMvc.perform(delete("/api/categories/" + category.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + category.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} should return 409 when category has active products")
    void shouldReturnConflictWhenCategoryHasActiveProducts() throws Exception {
        CategoryEntity category = categoryRepository.save(new CategoryEntity(
                "Electronics",
                "Electronic devices and accessories",
                true));

        productRepository.save(new ProductEntity(
                "Laptop QA Pro",
                "Laptop designed for automation testing practice",
                new BigDecimal("1200.00"),
                10,
                true,
                category));

        mockMvc.perform(delete("/api/categories/" + category.getId()))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is(
                        "Category with id " + category.getId() + " cannot be deleted because it has active products")));
    }

    @Test
    @DisplayName("DELETE /api/categories/{id} should return 404 when category does not exist")
    void shouldReturnNotFoundWhenDeletingCategoryDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/categories/999999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Category with id 999999 was not found")));
    }
}