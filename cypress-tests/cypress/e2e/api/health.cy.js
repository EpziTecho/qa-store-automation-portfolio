/*
 * ============================================================
 * File: health.cy.js
 * Module: API Smoke Tests
 *
 * Responsibility:
 * Validates that the QA Store API is running and responding correctly.
 *
 * Interaction:
 * Uses the custom command cy.apiHealthCheck() to call GET /api/health.
 * Depends on the backend running locally or through Docker at baseUrl.
 *
 * Design Pattern:
 * Smoke Test.
 *
 * Engineering Principles:
 * - Fast feedback.
 * - Simple first validation.
 * - Establishes Cypress-to-backend communication.
 * - Provides a baseline before adding full API regression tests.
 * ============================================================
 */

describe("Health API", () => {
    it("should return API status UP", () => {
        cy.apiHealthCheck().then((response) => {
            expect(response.status).to.eq(200);

            expect(response.body).to.have.property("status", "UP");
            expect(response.body).to.have.property("service", "qa-store-api");
            expect(response.body).to.have.property("version");
            expect(response.body).to.have.property("message");
        });
    });
});
