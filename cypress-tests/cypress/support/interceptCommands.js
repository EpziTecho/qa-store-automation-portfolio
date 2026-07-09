/*
 * ============================================================
 * File: interceptCommands.js
 * Module: Cypress Intercept Custom Commands
 *
 * Responsibility:
 * Defines reusable Cypress commands for network mocking, spying and failure
 * simulation using cy.intercept().
 *
 * Interaction:
 * Mocking specs call commands such as cy.mockGetProductsSuccess(),
 * cy.mockGetCategoriesServerError(), cy.spyGetHealth() and
 * cy.mockCreateProductSuccess().
 *
 * Design Pattern:
 * Command Pattern / Network Test Double.
 *
 * Engineering Principles:
 * - DRY: avoids repeated cy.intercept() definitions.
 * - Readability: specs describe network behavior using business intent.
 * - Maintainability: route matchers and mock bodies stay centralized.
 * - Reusability: commands can be reused by future UI automation tests.
 * ============================================================
 */

import { INTERCEPT_ROUTES } from "./interceptRoutes";

import {
    buildCreatedProductResponse,
    buildInternalServerError,
    buildMockedProductList,
    buildUnauthorizedError,
} from "./mockBuilders";

/*
 * Mocks GET /api/products with a successful product list response.
 */
Cypress.Commands.add(
    "mockGetProductsSuccess",
    (products = buildMockedProductList()) => {
        return cy
            .intercept("GET", INTERCEPT_ROUTES.products.base, {
                statusCode: 200,
                body: products,
            })
            .as("getMockedProducts");
    },
);

/*
 * Mocks GET /api/products with an artificial delay.
 *
 * Useful to validate loading states in future UI tests.
 */
Cypress.Commands.add("mockGetProductsWithDelay", (products, delay = 1500) => {
    return cy
        .intercept("GET", INTERCEPT_ROUTES.products.base, {
            statusCode: 200,
            delay,
            body: products,
        })
        .as("getProductsWithDelay");
});

/*
 * Mocks GET /api/categories with a simulated 500 response.
 */
Cypress.Commands.add("mockGetCategoriesServerError", () => {
    return cy
        .intercept("GET", INTERCEPT_ROUTES.categories.base, {
            statusCode: 500,
            body: buildInternalServerError("/api/categories"),
        })
        .as("getCategoriesFailure");
});

/*
 * Spies on GET /api/health without modifying the real backend response.
 */
Cypress.Commands.add("spyGetHealth", () => {
    return cy.intercept("GET", INTERCEPT_ROUTES.health).as("getHealth");
});

/*
 * Mocks POST /api/products with a successful 201 response.
 *
 * This command also validates the outgoing request payload and Authorization
 * header, which is useful for future UI form tests.
 */
Cypress.Commands.add("mockCreateProductSuccess", (productPayload) => {
    const mockedResponse = buildCreatedProductResponse(productPayload);

    return cy
        .intercept("POST", INTERCEPT_ROUTES.products.base, (req) => {
            expect(req.method).to.eq("POST");
            expect(req.headers).to.have.property("content-type");
            expect(req.headers["content-type"]).to.include("application/json");
            expect(req.headers).to.have.property(
                "authorization",
                "Bearer fake-jwt-token",
            );

            expect(req.body).to.deep.eq(productPayload);

            req.reply({
                statusCode: 201,
                body: mockedResponse,
            });
        })
        .as("createProduct");
});

/*
 * Mocks POST /api/products with a 401 response when Authorization header is
 * missing.
 */
Cypress.Commands.add("mockCreateProductUnauthorized", () => {
    return cy
        .intercept("POST", INTERCEPT_ROUTES.products.base, (req) => {
            expect(req.method).to.eq("POST");
            expect(req.headers).to.not.have.property("authorization");

            req.reply({
                statusCode: 401,
                body: buildUnauthorizedError("/api/products"),
            });
        })
        .as("createProductWithoutAuth");
});
