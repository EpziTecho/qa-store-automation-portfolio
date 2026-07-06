# Backend Architecture

## Responsibility

This document explains the backend architecture of the QA Store API.

The backend is the main system under test for the QA Automation portfolio.

## Current Architecture

The backend currently uses a layered architecture.

```text
Client / Test Tool
      |
      v
REST Controller
      |
      v
Service Interface
      |
      v
In-Memory Service Implementation
      |
      v
ConcurrentHashMap
```

## Main Packages

```text
com.qastore.api
├── health
├── product
└── common
```

## Package Responsibilities

| Package | Responsibility |
|---|---|
| `health` | Exposes technical service availability endpoint |
| `product` | Contains product API, DTOs, service abstraction and business errors |
| `common` | Contains shared error handling components |

## Product Module Design

The Product module is currently organized as follows:

```text
ProductController
      |
      v
ProductService
      |
      v
InMemoryProductService
      |
      v
Product
```

## Component Responsibilities

| Component | Responsibility |
|---|---|
| `ProductController` | Exposes REST endpoints for product operations |
| `ProductService` | Defines product business operations |
| `InMemoryProductService` | Temporary implementation using memory storage |
| `Product` | Internal domain model |
| `CreateProductRequest` | Input DTO for product creation |
| `ProductResponse` | Output DTO for product responses |
| `ProductNotFoundException` | Domain-specific exception |
| `GlobalExceptionHandler` | Converts exceptions into HTTP responses |
| `ErrorResponse` | Standard error contract |

## Why Use Layered Architecture?

Layered architecture was selected because it provides:

- Clear separation of responsibilities
- Better testability
- Easier maintenance
- Familiar structure for Spring Boot teams
- A good foundation for API testing and automation

## Dependency Direction

The controller depends on an abstraction:

```text
ProductController -> ProductService
```

It does not depend directly on:

```text
InMemoryProductService
```

This is important because the implementation can change later without changing the controller.

## Current Limitation

The current backend does not persist data permanently.

Products are stored in memory and reset when the application restarts.

This limitation is intentional because persistence will be introduced in the MySQL phase.

## Next Architectural Evolution

In the next phase, the architecture will evolve into:

```text
ProductController
      |
      v
ProductService
      |
      v
ProductRepository
      |
      v
MySQL Database
```

This will introduce:

- JPA entities
- Repositories
- Database configuration
- Persistence validations
- Integration tests with database dependency