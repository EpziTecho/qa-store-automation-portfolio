# ADR-006: Use JWT Authentication with Spring Security

## Status

Accepted

## Context

The QA Store API needs authentication and authorization to simulate a realistic enterprise backend.

The project must support:

- User login.
- Secure password storage.
- Stateless authentication.
- Protected endpoints.
- Role-based authorization.
- API testing with authenticated requests.
- Swagger support for secured endpoints.

The application is designed as a REST API, not a server-rendered web application.

## Decision

The project will use:

- Spring Security for authentication and authorization.
- BCrypt for password hashing.
- JWT access tokens for stateless authentication.
- A custom JWT filter based on `OncePerRequestFilter`.
- Role-based authorization using authorities such as `ROLE_ADMIN`.
- JSON error responses for `401 Unauthorized` and `403 Forbidden`.

The application will not use HTTP sessions for authentication.

Session policy:

```java
SessionCreationPolicy.STATELESS