/*
 * ============================================================
 * File: cypress.config.js
 * Module: Cypress Framework Configuration
 *
 * Responsibility:
 * Defines global Cypress configuration for API and future UI tests.
 *
 * Interaction:
 * Cypress reads this file before executing specs.
 * The baseUrl points to the QA Store API running locally or through Docker.
 *
 * Design Pattern:
 * Configuration Object.
 *
 * Engineering Principles:
 * - Centralized configuration.
 * - Environment-based execution.
 * - Avoids hardcoded URLs inside test specs.
 * - Prepares the framework for CI/CD execution.
 * ============================================================
 */

const { defineConfig } = require("cypress");

module.exports = defineConfig({
    e2e: {
        /*
         * baseUrl allows tests to use relative paths like /api/health.
         * This avoids repeating http://localhost:8080 in every test.
         */
        baseUrl: "http://localhost:8080",

        /*
         * supportFile loads custom commands and global test setup.
         */
        supportFile: "cypress/support/e2e.js",

        /*
         * specPattern defines where Cypress should look for test files.
         */
        specPattern: "cypress/e2e/**/*.cy.js",

        /*
         * Screenshots and videos are useful for UI tests.
         * For now we disable video to keep local runs lighter.
         */
        video: false,

        /*
         * Default timeout for commands.
         * We keep a reasonable value for local Docker execution.
         */
        defaultCommandTimeout: 10000,

        /*
         * Environment variables available through Cypress.env().
         * These are non-sensitive defaults. Sensitive values should be passed
         * from the CLI or CI/CD environment later.
         */
        env: {
            apiBasePath: "/api",
            adminEmail: "admin@qastore.com",
            adminPassword: "Admin12345",
        },
    },
});
