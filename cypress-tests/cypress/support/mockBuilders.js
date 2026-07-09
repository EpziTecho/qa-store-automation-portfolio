/*
 * ============================================================
 * File: mockBuilders.js
 * Module: Cypress Mock Response Builders
 *
 * Responsibility:
 * Provides reusable builders for mocked API responses used with cy.intercept().
 *
 * Interaction:
 * Mocking specs import these builders to generate deterministic response bodies
 * without duplicating mock structures across tests.
 *
 * Design Pattern:
 * Test Data Builder.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated mock response objects.
 * - Maintainability: response contract changes are updated in one place.
 * - Consistency: mocked responses follow the same API structure.
 * - Reusability: builders can be reused across multiple intercept specs.
 * ============================================================
 */

export const buildApiErrorResponse = ({
    status,
    error,
    message,
    path,
    details = [],
}) => {
    return {
        timestamp: new Date().toISOString(),
        status,
        error,
        message,
        path,
        details,
    };
};

export const buildMockedProduct = (overrides = {}) => {
    return {
        id: 1001,
        name: "Mocked Cypress Product",
        description: "Product returned from Cypress mock builder",
        price: 999.9,
        stock: 15,
        categoryId: 501,
        categoryName: "Mocked Category",
        ...overrides,
    };
};

export const buildMockedProductList = () => {
    return [
        buildMockedProduct({
            id: 1001,
            name: "Mocked Cypress Laptop",
            categoryName: "Mocked Electronics",
        }),
        buildMockedProduct({
            id: 1002,
            name: "Mocked Cypress Mouse",
            description: "Second product returned from Cypress mock builder",
            price: 49.9,
            stock: 40,
            categoryId: 502,
            categoryName: "Mocked Accessories",
        }),
    ];
};

export const buildCreatedProductResponse = (productPayload, overrides = {}) => {
    return {
        id: 9001,
        name: productPayload.name,
        description: productPayload.description,
        price: productPayload.price,
        stock: productPayload.stock,
        categoryId: productPayload.categoryId,
        categoryName: "Intercept Category",
        ...overrides,
    };
};

export const buildUnauthorizedError = (path) => {
    return buildApiErrorResponse({
        status: 401,
        error: "Unauthorized",
        message: "Authentication is required",
        path,
    });
};

export const buildInternalServerError = (path) => {
    return buildApiErrorResponse({
        status: 500,
        error: "Internal Server Error",
        message: "Simulated server failure from Cypress intercept",
        path,
    });
};
