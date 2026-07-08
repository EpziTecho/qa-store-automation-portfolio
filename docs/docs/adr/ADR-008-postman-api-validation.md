# ADR-008: Use Postman for API Validation and Regression Support

## Status

Accepted

## Context

The QA Store API needs a practical tool to validate endpoints during development and before implementing deeper automated API testing.

The project already includes:

- Spring Boot REST API.
- MySQL persistence.
- JWT authentication.
- Role-based authorization.
- Swagger/OpenAPI documentation.
- Docker local environment.

The next step is to provide an API client and validation suite that can be used manually and semi-automatically.

## Decision

The project will use Postman to create a reusable API validation collection.

The collection will include:

- Health checks.
- Authentication requests.
- JWT token storage.
- Public endpoint validation.
- Protected endpoint validation.
- Category CRUD workflows.
- Product CRUD workflows.
- Positive scenarios.
- Negative scenarios.
- Dynamic environment variables.

The project will version:

```text
postman/collections/qa-store-api.postman_collection.json
postman/environments/qa-store-local.postman_environment.json