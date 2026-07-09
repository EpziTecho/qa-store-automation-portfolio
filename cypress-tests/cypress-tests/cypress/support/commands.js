/*
 * ============================================================
 * File: commands.js
 * Module: Cypress Custom Commands
 *
 * Responsibility:
 * Defines reusable Cypress commands for QA Store API tests.
 *
 * Interaction:
 * API specs can call custom commands such as cy.apiHealthCheck().
 * The commands are loaded globally through cypress/support/e2e.js.
 *
 * Design Pattern:
 * Command Pattern.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated request logic.
 * - Readability: tests express business intent more clearly.
 * - Reusability: common API actions are centralized.
 * ============================================================
 */

/*
 * Performs a health check request against the QA Store API.
 *
 * Usage:
 * cy.apiHealthCheck()
 */
Cypress.Commands.add("apiHealthCheck", () => {
    return cy.request({
        method: "GET",
        url: "/api/health",
        failOnStatusCode: false,
    });
});
