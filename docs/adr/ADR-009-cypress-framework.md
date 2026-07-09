# ADR-009: Use Cypress as API and Future UI Automation Framework

## Status

Accepted

## Context

The QA Store Automation Portfolio requires a JavaScript-based automation framework to validate API behavior and support future UI automation.

The backend already includes:

- Spring Boot API.
- MySQL persistence.
- JWT authentication.
- Role-based authorization.
- Docker local environment.
- Postman API validation.

The next step is to establish a maintainable Cypress framework.

## Decision

The project will use Cypress as an automation framework for:

- API smoke testing.
- API authentication testing.
- Future API regression testing.
- Future UI automation.
- Future mocking and intercept scenarios.

The Cypress project will live in:

```text
cypress-tests/