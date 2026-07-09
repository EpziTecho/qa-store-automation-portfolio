# Cypress Framework Guide - QA Store Automation Portfolio

## 1. Purpose

This document explains the Cypress framework structure used in the QA Store Automation Portfolio.

Cypress is used as an automation framework for API testing and future UI testing.

The goal of this phase is to establish a clean, maintainable and scalable test framework before implementing the full API regression suite.

---

## 2. Current Scope

The current Cypress framework includes:

- API smoke testing.
- JWT authentication testing.
- Custom Cypress commands.
- Fixture-based test data.
- Data factory helpers.
- Basic framework validation tests.

Full API regression coverage will be implemented in the next phase.

---

## 3. Project Structure

```text
cypress-tests/
├── cypress/
│   ├── e2e/
│   │   └── api/
│   │       ├── health.cy.js
│   │       ├── authentication.cy.js
│   │       └── framework-setup.cy.js
│   │
│   ├── fixtures/
│   │   ├── auth/
│   │   │   └── invalid-login.json
│   │   ├── categories/
│   │   │   ├── create-category.json
│   │   │   └── invalid-category.json
│   │   └── products/
│   │       ├── create-product.json
│   │       └── invalid-product.json
│   │
│   └── support/
│       ├── commands.js
│       ├── dataFactory.js
│       └── e2e.js
│
├── cypress.config.js
├── package.json
├── package-lock.json
└── README.md