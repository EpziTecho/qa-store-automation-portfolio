# ADR-011: Use Cypress cy.intercept for Network Mocking and Request Validation

## Status

Accepted

## Context

The QA Store project already includes:

- Spring Boot backend.
- MySQL persistence.
- JWT authentication.
- Docker environment.
- Postman API validation.
- Cypress API regression tests.

The next testing capability required is network control for future UI automation.

The project needs a way to:

- Mock backend responses.
- Simulate backend failures.
- Simulate network delays.
- Validate outgoing frontend requests.
- Spy on real API calls.
- Build deterministic frontend test scenarios.

## Decision

The project will use Cypress `cy.intercept()` for network mocking, spying and request validation.

The implementation includes:

- Mocking specs under `cypress/e2e/mocking`.
- Centralized intercept route matchers.
- Centralized mock builders.
- Reusable intercept custom commands.
- Browser-level request execution using `window.fetch()` for demonstration purposes.

## Important Technical Decision

`cy.intercept()` will be used for browser/application network calls.

It will not replace `cy.request()`.

The project uses both tools with different responsibilities:

```text
cy.request()    → direct API testing
cy.intercept()  → browser/application request control