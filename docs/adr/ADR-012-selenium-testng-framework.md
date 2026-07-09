# ADR-012: Use Selenium WebDriver with TestNG for Java UI Automation

## Status

Accepted

## Context

The QA Store project already includes:

- Spring Boot backend.
- MySQL persistence.
- JWT authentication.
- Docker environment.
- Postman API validation.
- Cypress API tests.
- Cypress mocking and intercept tests.

The portfolio also requires Java-based UI automation skills.

## Decision

The project will include a dedicated Selenium + TestNG module under:

```text
selenium-tests/