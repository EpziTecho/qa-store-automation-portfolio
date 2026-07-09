/*
 * ============================================================
 * File: commands.js
 * Module: Cypress Custom Commands
 *
 * Responsibility:
 * Defines reusable Cypress commands for QA Store API tests.
 *
 * Interaction:
 * API specs call custom commands such as cy.apiHealthCheck(),
 * cy.apiLoginAsAdmin(), cy.apiCreateCategory() and cy.apiCreateProduct().
 * This file uses API_ROUTES from apiRoutes.js to avoid hardcoded endpoints.
 *
 * Design Pattern:
 * Command Pattern.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated request logic.
 * - Readability: specs express test intent instead of HTTP implementation details.
 * - Reusability: authentication and API actions are centralized.
 * - Maintainability: endpoint changes are handled through apiRoutes.js.
 * ============================================================
 */

import { API_ROUTES } from "./apiRoutes";

/*
 * Builds an Authorization header for Bearer JWT requests.
 *
 * Keeping this helper local avoids duplicating the same object structure in
 * every authenticated command.
 */
const bearerAuthHeader = (token) => {
    return token ? { Authorization: `Bearer ${token}` } : {};
};

/*
 * Performs a health check request against the QA Store API.
 */
Cypress.Commands.add("apiHealthCheck", () => {
    return cy.request({
        method: "GET",
        url: API_ROUTES.health,
        failOnStatusCode: false,
    });
});

/*
 * Authenticates the default local admin user.
 *
 * After successful login, the JWT access token is stored in Cypress.env().
 */
Cypress.Commands.add("apiLoginAsAdmin", () => {
    const adminEmail = Cypress.env("adminEmail");
    const adminPassword = Cypress.env("adminPassword");

    return cy
        .request({
            method: "POST",
            url: API_ROUTES.auth.login,
            failOnStatusCode: false,
            body: {
                email: adminEmail,
                password: adminPassword,
            },
        })
        .then((response) => {
            if (response.status === 200 && response.body.accessToken) {
                Cypress.env("accessToken", response.body.accessToken);
            }

            return response;
        });
});

/*
 * Retrieves the current authenticated user.
 */
Cypress.Commands.add(
    "apiGetCurrentUser",
    (token = Cypress.env("accessToken")) => {
        return cy.request({
            method: "GET",
            url: API_ROUTES.auth.me,
            headers: bearerAuthHeader(token),
            failOnStatusCode: false,
        });
    },
);

/*
 * Retrieves all active categories.
 */
Cypress.Commands.add("apiGetAllCategories", () => {
    return cy.request({
        method: "GET",
        url: API_ROUTES.categories.base,
        failOnStatusCode: false,
    });
});

/*
 * Creates a category using an authenticated admin token.
 */
Cypress.Commands.add("apiCreateCategory", (categoryPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "POST",
        url: API_ROUTES.categories.base,
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
        body: categoryPayload,
    });
});

/*
 * Creates a category without sending JWT.
 *
 * Used to validate 401 Unauthorized scenarios.
 */
Cypress.Commands.add("apiCreateCategoryWithoutToken", (categoryPayload) => {
    return cy.request({
        method: "POST",
        url: API_ROUTES.categories.base,
        failOnStatusCode: false,
        body: categoryPayload,
    });
});

/*
 * Retrieves a category by ID.
 */
Cypress.Commands.add("apiGetCategoryById", (categoryId) => {
    return cy.request({
        method: "GET",
        url: API_ROUTES.categories.byId(categoryId),
        failOnStatusCode: false,
    });
});

/*
 * Updates a category by ID using an authenticated admin token.
 */
Cypress.Commands.add("apiUpdateCategory", (categoryId, categoryPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "PUT",
        url: API_ROUTES.categories.byId(categoryId),
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
        body: categoryPayload,
    });
});

/*
 * Deletes a category by ID using an authenticated admin token.
 */
Cypress.Commands.add("apiDeleteCategory", (categoryId) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "DELETE",
        url: API_ROUTES.categories.byId(categoryId),
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
    });
});

/*
 * Retrieves all active products.
 */
Cypress.Commands.add("apiGetAllProducts", () => {
    return cy.request({
        method: "GET",
        url: API_ROUTES.products.base,
        failOnStatusCode: false,
    });
});

/*
 * Creates a product using an authenticated admin token.
 */
Cypress.Commands.add("apiCreateProduct", (productPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "POST",
        url: API_ROUTES.products.base,
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
        body: productPayload,
    });
});

/*
 * Creates a product without sending JWT.
 *
 * Used to validate 401 Unauthorized scenarios.
 */
Cypress.Commands.add("apiCreateProductWithoutToken", (productPayload) => {
    return cy.request({
        method: "POST",
        url: API_ROUTES.products.base,
        failOnStatusCode: false,
        body: productPayload,
    });
});

/*
 * Retrieves a product by ID.
 */
Cypress.Commands.add("apiGetProductById", (productId) => {
    return cy.request({
        method: "GET",
        url: API_ROUTES.products.byId(productId),
        failOnStatusCode: false,
    });
});

/*
 * Updates a product by ID using an authenticated admin token.
 */
Cypress.Commands.add("apiUpdateProduct", (productId, productPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "PUT",
        url: API_ROUTES.products.byId(productId),
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
        body: productPayload,
    });
});

/*
 * Deletes a product by ID using an authenticated admin token.
 */
Cypress.Commands.add("apiDeleteProduct", (productId) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "DELETE",
        url: API_ROUTES.products.byId(productId),
        headers: bearerAuthHeader(token),
        failOnStatusCode: false,
    });
});
