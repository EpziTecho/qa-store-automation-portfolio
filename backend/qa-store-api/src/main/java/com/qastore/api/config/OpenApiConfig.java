package com.qastore.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
 * Configures the OpenAPI metadata exposed by Swagger UI.
 *
 * Interaction:
 * Springdoc OpenAPI reads this bean and enriches the generated API
 * documentation with project-level information such as title, version,
 * description, contact, license and server URLs.
 *
 * Design Pattern:
 * Configuration Class.
 *
 * Engineering Principles:
 * - Separation of concerns: documentation configuration is isolated from controllers.
 * - Single Responsibility Principle: this class only configures OpenAPI metadata.
 * - Maintainability: API documentation metadata can be updated in one place.
 * ============================================================
 */

@Configuration
public class OpenApiConfig {

    /*
     * Defines the OpenAPI documentation metadata.
     *
     * Spring registers this bean in the application context.
     * Springdoc uses it when generating the /v3/api-docs contract and Swagger UI.
     */
    @Bean
    public OpenAPI qaStoreOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers());
    }

    /*
     * Defines general information about the API.
     *
     * This information appears at the top of Swagger UI.
     */
    private Info apiInfo() {
        return new Info()
                .title("QA Store API")
                .version("1.0.0")
                .description("""
                        QA Store API is a Spring Boot backend designed for a professional QA Automation portfolio.

                        It includes product and category management, MySQL persistence, JPA relationships,
                        validation, soft delete and business rules that can be tested through API automation.
                        """)
                .contact(apiContact())
                .license(apiLicense());
    }

    /*
     * Defines project contact information.
     *
     * Replace the email and URL later with your professional GitHub or LinkedIn information.
     */
    private Contact apiContact() {
        return new Contact()
                .name("QA Automation Portfolio")
                .email("portfolio@example.com")
                .url("https://github.com/");
    }

    /*
     * Defines the license metadata shown in Swagger UI.
     */
    private License apiLicense() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /*
     * Defines the available API server URLs.
     *
     * For now we only document the local development server.
     * Later, when Docker and deploy are added, more servers can be included.
     */
    private List<Server> apiServers() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local development server");

        return List.of(localServer);
    }
}