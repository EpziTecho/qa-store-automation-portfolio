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
 * cy.apiLoginAsAdmin(), cy.apiGetCurrentUser(), cy.apiCreateCategory()
 * and cy.apiCreateProduct().
 *
 * Design Pattern:
 * Command Pattern.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated request logic.
 * - Readability: specs express business intent instead of HTTP implementation details.
 * - Reusability: authentication and API actions are centralized.
 * - Maintainability: endpoint changes are updated in one place.
 * ============================================================
 */

Cypress.Commands.add("apiHealthCheck", () => {
    return cy.request({
        method: "GET",
        url: "/api/health",
        failOnStatusCode: false,
    });
});

Cypress.Commands.add("apiLoginAsAdmin", () => {
    const adminEmail = Cypress.env("adminEmail");
    const adminPassword = Cypress.env("adminPassword");

    return cy
        .request({
            method: "POST",
            url: "/api/auth/login",
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

Cypress.Commands.add(
    "apiGetCurrentUser",
    (token = Cypress.env("accessToken")) => {
        const headers = token ? { Authorization: `Bearer ${token}` } : {};

        return cy.request({
            method: "GET",
            url: "/api/auth/me",
            headers,
            failOnStatusCode: false,
        });
    },
);

/*
 * Creates a category using an authenticated admin token.
 *
 * Requires:
 * - cy.apiLoginAsAdmin() to be executed before this command.
 */
Cypress.Commands.add("apiCreateCategory", (categoryPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "POST",
        url: "/api/categories",
        headers: {
            Authorization: `Bearer ${token}`,
        },
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
        url: `/api/categories/${categoryId}`,
        failOnStatusCode: false,
    });
});

/*
 * Deletes a category by ID using an authenticated admin token.
 *
 * This is useful for cleanup in API tests.
 */
Cypress.Commands.add("apiDeleteCategory", (categoryId) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "DELETE",
        url: `/api/categories/${categoryId}`,
        headers: {
            Authorization: `Bearer ${token}`,
        },
        failOnStatusCode: false,
    });
});

/*
 * Creates a product using an authenticated admin token.
 *
 * Requires:
 * - A valid categoryId in the payload.
 * - cy.apiLoginAsAdmin() executed before this command.
 */
Cypress.Commands.add("apiCreateProduct", (productPayload) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "POST",
        url: "/api/products",
        headers: {
            Authorization: `Bearer ${token}`,
        },
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
        url: `/api/products/${productId}`,
        failOnStatusCode: false,
    });
});

/*
 * Deletes a product by ID using an authenticated admin token.
 */
Cypress.Commands.add("apiDeleteProduct", (productId) => {
    const token = Cypress.env("accessToken");

    return cy.request({
        method: "DELETE",
        url: `/api/products/${productId}`,
        headers: {
            Authorization: `Bearer ${token}`,
        },
        failOnStatusCode: false,
    });
});

/*
 * Retrieves all active categories.
 */
Cypress.Commands.add("apiGetAllCategories", () => {
    return cy.request({
        method: "GET",
        url: "/api/categories",
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
        url: `/api/categories/${categoryId}`,
        headers: {
            Authorization: `Bearer ${token}`,
        },
        failOnStatusCode: false,
        body: categoryPayload,
    });
});

/*
 * Creates a category without sending JWT.
 *
 * This command is used to validate 401 Unauthorized scenarios.
 */
Cypress.Commands.add("apiCreateCategoryWithoutToken", (categoryPayload) => {
    return cy.request({
        method: "POST",
        url: "/api/categories",
        failOnStatusCode: false,
        body: categoryPayload,
    });
});
