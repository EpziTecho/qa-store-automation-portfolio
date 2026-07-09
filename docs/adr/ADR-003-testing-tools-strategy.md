# ADR-003: Use Multiple Testing Tools with Clear Responsibilities

## Status

Accepted

## Context

The QA Store Automation Portfolio must demonstrate professional knowledge of different QA Automation tools.

However, using many tools without clear responsibilities can make the project confusing and redundant.

The following tools were considered:

- Postman
- REST Assured
- Cypress
- Selenium + TestNG
- JUnit
- Spring Boot Test

## Decision

Use multiple testing tools, but assign each one a specific responsibility.

## Tool Responsibilities

| Tool | Responsibility |
|---|---|
| JUnit | Unit testing for backend logic |
| Spring Boot Test | Integration testing inside the backend |
| Postman | API exploration and collection-based testing |
| REST Assured | Java-based API regression automation |
| Cypress | UI automation, API checks and mocking |
| Selenium + TestNG | Enterprise-style Java UI automation |
| GitHub Actions | Automated execution in CI/CD |

## Rationale

Different companies use different QA Automation stacks.

This project intentionally includes both modern and enterprise-oriented tools to demonstrate technical versatility.

Cypress is valuable for modern web testing because it provides fast feedback, strong debugging tools and network control with `cy.intercept()`.

Selenium with TestNG is valuable because many enterprise teams still use Java-based UI automation frameworks.

Postman is useful for API exploration and initial validation.

REST Assured is useful for maintainable API regression testing in Java-based environments.

## Consequences

### Positive Consequences

- Demonstrates broad QA Automation knowledge.
- Shows ability to select tools based on responsibility.
- Supports different testing levels.
- Provides stronger interview material.

### Negative Consequences

- Requires clear documentation to avoid redundancy.
- Requires discipline to avoid duplicating the same tests in every tool.
- Increases maintenance effort.

## Final Decision

The project will use multiple testing tools, but each tool will have a clearly documented testing responsibility.