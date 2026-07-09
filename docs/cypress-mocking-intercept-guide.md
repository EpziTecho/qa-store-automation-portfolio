# Cypress Mocking and Intercept Guide - QA Store Automation Portfolio

## 1. Purpose

This document explains how Cypress `cy.intercept()` is used in the QA Store Automation Portfolio.

The goal of this phase is to demonstrate professional network control techniques for future UI automation and controlled frontend/API testing scenarios.

---

## 2. What is cy.intercept()?

`cy.intercept()` is a Cypress command used to spy, stub, mock or modify HTTP requests made by the browser during test execution.

It is mainly used to:

- Observe network requests.
- Validate outgoing request payloads.
- Mock backend responses.
- Simulate backend errors.
- Simulate network latency.
- Create deterministic frontend test scenarios.

---

## 3. Important Difference: cy.request() vs cy.intercept()

### cy.request()

`cy.request()` sends HTTP requests directly from Cypress to the backend.

It is useful for:

- API testing.
- Backend validation.
- Test data setup.
- Test data cleanup.
- Direct contract validation.

Example:

```javascript
cy.request({
  method: 'GET',
  url: '/api/products'
});