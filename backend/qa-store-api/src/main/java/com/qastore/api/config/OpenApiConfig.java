package com.qastore.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/*
 * ============================================================
 * File: OpenApiConfig.java
 * Module: API Documentation
 *
 * Responsibility:
 * Configures the OpenAPI metadata and JWT security scheme exposed by Swagger UI.
 *
 * Interaction:
 * Springdoc OpenAPI reads this bean and enriches the generated API
 * documentation with project-level information and Bearer JWT authentication.
 *
 * Design Pattern:
 * Configuration Class.
 *
 * Engineering Principles:
 * - Separation of concerns: documentation configuration is isolated from controllers.
 * - Single Responsibility Principle: this class only configures OpenAPI metadata.
 * - Maintainability: API documentation and security metadata can be updated in one place.
 * - Security documentation: protected endpoints can reference the bearerAuth scheme.
 * ============================================================
 */

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    /*
     * Defines the OpenAPI documentation metadata.
     *
     * Springdoc uses this bean when generating /v3/api-docs and Swagger UI.
     */
    @Bean
    public OpenAPI qaStoreOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .components(securityComponents());
    }

    private Info apiInfo() {
        return new Info()
                .title("QA Store API")
                .version("1.0.0")
                .description("QA Store API is a Spring Boot backend designed for a professional QA Automation portfolio.\n\n"
                        + "It includes product and category management, MySQL persistence, JPA relationships,\n"
                        + "validation, soft delete, JWT authentication and role-based authorization.")
                .contact(apiContact())
                .license(apiLicense());
    }

    private Contact apiContact() {
        return new Contact()
                .name("QA Automation Portfolio")
                .email("portfolio@example.com")
                .url("https://github.com/");
    }

    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

        return List.of(localServer);
    }

    /*
     * Defines the Bearer JWT security scheme used by protected endpoints.
     *
     * Swagger UI will display the Authorize button based on this configuration.
     * The user should paste only the JWT token value. Swagger UI will add the
     * "Bearer " prefix automatically.
     */
    private Components securityComponents() {
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste a valid JWT access token obtained from POST /api/auth/login");

        return new Components()
                .addSecuritySchemes(BEARER_AUTH, bearerAuthScheme);
    }
}