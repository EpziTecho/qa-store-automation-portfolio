/*
 * ============================================================
 * File: authentication.cy.js
 * Module: API Authentication Tests
 *
 * Responsibility:
 * Validates JWT authentication behavior for the QA Store API.
 *
 * Interaction:
 * Uses Cypress custom commands to call:
 * - POST /api/auth/login
 * - GET /api/auth/me
 *
 * Depends on the backend running locally or through Docker at the configured
 * baseUrl.
 *
 * Design Pattern:
 * API Integration Test.
 *
 * Engineering Principles:
 * - Reusable authentication flow.
 * - Positive and negative security validation.
 * - Stateless JWT testing.
 * - Clear separation between setup commands and assertions.
 * ============================================================
 */

import {
    assertInvalidCredentialsError,
    assertUnauthorizedError,
} from "../../support/apiAssertions";

describe("Authentication API", () => {
    it("should login as admin and return a JWT access token", () => {
        cy.apiLoginAsAdmin().then((response) => {
            expect(response.status).to.eq(200);

            expect(response.body).to.have.property("authenticated", true);
            expect(response.body).to.have.property(
                "email",
                Cypress.env("adminEmail"),
            );
            expect(response.body).to.have.property("accessToken").and.not.be
                .empty;
            expect(response.body).to.have.property("tokenType", "Bearer");
            expect(response.body).to.have.property("expiresInMs");

            expect(response.body.roles).to.include("ROLE_ADMIN");

            expect(Cypress.env("accessToken")).to.eq(response.body.accessToken);
        });
    });

    it("should return current authenticated user when JWT is valid", () => {
        cy.apiLoginAsAdmin();

        cy.apiGetCurrentUser().then((response) => {
            expect(response.status).to.eq(200);

            expect(response.body).to.have.property("userId");
            expect(response.body).to.have.property(
                "email",
                Cypress.env("adminEmail"),
            );
            expect(response.body.roles).to.include("ROLE_ADMIN");
        });
    });

    it("should return 401 when accessing current user without JWT", () => {
        cy.apiGetCurrentUser(null).then((response) => {
            assertUnauthorizedError(response, "/api/auth/me");
        });
    });

    it("should return 401 when accessing current user with invalid JWT", () => {
        cy.apiGetCurrentUser("invalid.token.value").then((response) => {
            assertUnauthorizedError(response, "/api/auth/me");
        });
    });

    it("should return 401 when login password is invalid", () => {
        cy.request({
            method: "POST",
            url: "/api/auth/login",
            failOnStatusCode: false,
            body: {
                email: Cypress.env("adminEmail"),
                password: "WrongPassword123",
            },
        }).then((response) => {
            assertInvalidCredentialsError(response);
        });
    });
});
