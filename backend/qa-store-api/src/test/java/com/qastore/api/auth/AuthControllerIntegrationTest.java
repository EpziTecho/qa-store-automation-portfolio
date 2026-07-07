package com.qastore.api.auth;

import com.qastore.api.user.RoleEntity;
import com.qastore.api.user.RoleName;
import com.qastore.api.user.RoleRepository;
import com.qastore.api.user.UserEntity;
import com.qastore.api.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

/*
 * ============================================================
 * File: AuthControllerIntegrationTest.java
 * Module: Authentication Tests
 *
 * Responsibility:
 * Validates the authentication endpoint using the real Spring application context.
 *
 * Interaction:
 * Uses MockMvc to execute HTTP requests against AuthController.
 * Uses UserRepository and RoleRepository to prepare authentication test data.
 * Uses PasswordEncoder to store test passwords as BCrypt hashes.
 *
 * Design Pattern:
 * Integration Test.
 *
 * Engineering Principles:
 * - Validates controller-service-repository integration.
 * - Validates secure password comparison using BCrypt.
 * - Validates consistent 401 responses for invalid credentials.
 * - Prevents authentication regressions before JWT is introduced.
 * ============================================================
 */

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/auth/login should authenticate user when credentials are valid")
    void shouldAuthenticateUserWhenCredentialsAreValid() throws Exception {
        RoleEntity adminRole = roleRepository.save(new RoleEntity(
                RoleName.ROLE_ADMIN,
                "Administrator role"));

        UserEntity adminUser = new UserEntity(
                "System",
                "Administrator",
                "admin@qastore.com",
                passwordEncoder.encode("Admin12345"),
                true);

        adminUser.addRole(adminRole);

        userRepository.save(adminUser);

        String requestBody = """
                {
                  "email": "admin@qastore.com",
                  "password": "Admin12345"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authenticated", is(true)))
                .andExpect(jsonPath("$.email", is("admin@qastore.com")))
                .andExpect(jsonPath("$.roles", contains("ROLE_ADMIN")))
                .andExpect(jsonPath("$.message", is("Login successful")));
    }

    @Test
    @DisplayName("POST /api/auth/login should return 401 when password is invalid")
    void shouldReturnUnauthorizedWhenPasswordIsInvalid() throws Exception {
        RoleEntity adminRole = roleRepository.save(new RoleEntity(
                RoleName.ROLE_ADMIN,
                "Administrator role"));

        UserEntity adminUser = new UserEntity(
                "System",
                "Administrator",
                "admin@qastore.com",
                passwordEncoder.encode("Admin12345"),
                true);

        adminUser.addRole(adminRole);

        userRepository.save(adminUser);

        String requestBody = """
                {
                  "email": "admin@qastore.com",
                  "password": "WrongPassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("Unauthorized")))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    @DisplayName("POST /api/auth/login should return 401 when email does not exist")
    void shouldReturnUnauthorizedWhenEmailDoesNotExist() throws Exception {
        String requestBody = """
                {
                  "email": "unknown@qastore.com",
                  "password": "Admin12345"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("Unauthorized")))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    @DisplayName("POST /api/auth/login should return 401 when user is inactive")
    void shouldReturnUnauthorizedWhenUserIsInactive() throws Exception {
        RoleEntity userRole = roleRepository.save(new RoleEntity(
                RoleName.ROLE_USER,
                "Standard user role"));

        UserEntity inactiveUser = new UserEntity(
                "Inactive",
                "User",
                "inactive@qastore.com",
                passwordEncoder.encode("User12345"),
                false);

        inactiveUser.addRole(userRole);

        userRepository.save(inactiveUser);

        String requestBody = """
                {
                  "email": "inactive@qastore.com",
                  "password": "User12345"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    @DisplayName("POST /api/auth/login should return 400 when request is invalid")
    void shouldReturnBadRequestWhenLoginRequestIsInvalid() throws Exception {
        String requestBody = """
                {
                  "email": "",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Request validation failed")));
    }
}