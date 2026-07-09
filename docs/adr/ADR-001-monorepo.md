# ADR-001: Use a Monorepo Structure

## Status

Accepted

## Context

The QA Store Automation Portfolio is designed as a professional QA Automation ecosystem.

The project includes multiple technical components:

- Spring Boot backend
- MySQL database
- Cypress tests
- Selenium + TestNG tests
- REST Assured tests
- Postman collections
- Docker configuration
- GitHub Actions workflows
- Technical documentation

A decision was required regarding how to organize these components in GitHub.

The main alternatives were:

1. Use separate repositories for each component.
2. Use a single monorepo containing all project components.

## Decision

Use a monorepo structure.

The repository will be organized as follows:

```text
qa-store-automation-portfolio/
│
├── backend/
├── cypress-tests/
├── selenium-tests/
├── rest-assured-tests/
├── postman/
├── docker/
├── docs/
└── .github/workflows/