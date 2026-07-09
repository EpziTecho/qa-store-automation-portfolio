# ADR-005: Use Springdoc OpenAPI for API Documentation

## Status

Accepted

## Context

The QA Store API exposes REST endpoints for health checks, products and categories.

As the project grows, the API contract needs to be understandable, testable and easy to share with different technical roles such as backend developers, QA engineers, frontend developers and DevOps engineers.

Manual documentation can become outdated quickly if it is not connected to the source code.

## Decision

The project will use Springdoc OpenAPI to generate an OpenAPI 3 contract from the Spring Boot application.

Swagger UI will be exposed locally at:

```text
http://localhost:8080/swagger-ui.html