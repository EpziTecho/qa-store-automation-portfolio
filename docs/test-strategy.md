# Test Strategy

## Responsibility

This document defines the testing strategy for the QA Store Automation Portfolio.

The purpose is to explain what will be tested, how it will be tested and which tools will be used for each testing level.

## Testing Pyramid

```text
                UI E2E Tests
          Cypress / Selenium + TestNG

                API Tests
          Postman / REST Assured

        Integration and Unit Tests
          JUnit / Spring Boot Test
```

## Testing Tools

| Testing Goal | Tool |
|---|---|
| API exploration | Postman |
| API collection testing | Postman / Newman |
| API automation with Java | REST Assured |
| UI automation modern stack | Cypress |
| UI automation enterprise Java stack | Selenium + TestNG |
| Mocking API responses | Cypress cy.intercept() |
| Backend unit tests | JUnit |
| Backend integration tests | Spring Boot Test |
| CI/CD execution | GitHub Actions |

## Testing Scope

The project will include:

- Positive test cases
- Negative test cases
- Authentication scenarios
- Authorization scenarios
- CRUD validations
- Contract validations
- UI flows
- Mocked scenarios
- Regression suite
- Smoke suite
- CI/CD validations

## Test Types

### Unit Tests

Unit tests validate isolated pieces of backend logic.

Example:

```text
Validate that the product service calculates product availability correctly.
```

### Integration Tests

Integration tests validate that different layers work together.

Example:

```text
Validate that ProductController, ProductService, ProductRepository and MySQL interact correctly.
```

### API Tests

API tests validate HTTP behavior.

Example:

```text
POST /api/products should return 201 Created when the request body is valid.
```

### UI Tests

UI tests validate business flows from the user interface.

Example:

```text
A user logs in, views the product list and creates an order.
```

### Mocking Tests

Mocking tests validate UI behavior when backend responses are controlled.

Example:

```text
Simulate an empty product list using cy.intercept().
```

## Quality Goal

The goal is not only to automate test cases, but to design a maintainable and scalable QA Automation ecosystem.

Each testing tool must have a clear responsibility.
No tool should be used just for demonstration without technical purpose.