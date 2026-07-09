/*
 * ============================================================
 * File: request-validation.cy.js
 * Module: Cypress Request Validation with cy.intercept
 *
 * Responsibility:
 * Demonstrates how to validate outgoing HTTP requests using reusable
 * cy.intercept() custom commands.
 *
 * Interaction:
 * Executes browser-level fetch() calls from a valid application page and
 * validates them through intercept commands.
 *
 * Design Pattern:
 * Network Spy / Network Stub.
 *
 * Engineering Principles:
 * - Observability: validates the exact request sent by the client.
 * - Isolation: avoids depending on real database state.
 * - Determinism: mocked responses make tests predictable.
 * - Contract validation: verifies request body, headers and response handling.
 * ============================================================
 */

import {
    buildCreatedProductResponse,
    buildMockedProduct,
} from "../../support/mockBuilders";

describe("Request validation with cy.intercept", () => {
    const visitBrowserContext = () => {
        cy.visit("/swagger-ui.html");
    };

    it("should validate POST /api/products request body and mock a 201 response", () => {
        const productPayload = {
            name: "Intercept Product",
            description: "Product created through intercepted browser request",
            price: 249.9,
            stock: 12,
            categoryId: 777,
        };

        const expectedResponse = buildCreatedProductResponse(productPayload);

        cy.mockCreateProductSuccess(productPayload);

        visitBrowserContext();

        cy.window().then((window) => {
            return window
                .fetch("/api/products", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer fake-jwt-token",
                    },
                    body: JSON.stringify(productPayload),
                })
                .then((response) => {
                    expect(response.status).to.eq(201);

                    return response.json();
                })
                .then((body) => {
                    expect(body).to.deep.eq(expectedResponse);
                });
        });

        cy.wait("@createProduct").then((interception) => {
            expect(interception.request.method).to.eq("POST");
            expect(interception.request.body).to.deep.eq(productPayload);
            expect(interception.response.statusCode).to.eq(201);
            expect(interception.response.body).to.deep.eq(expectedResponse);
        });
    });

    it("should simulate 401 Unauthorized when Authorization header is missing", () => {
        const productPayload = {
            name: "Unauthorized Intercept Product",
            description: "This request does not send JWT",
            price: 99.9,
            stock: 5,
            categoryId: 777,
        };

        cy.mockCreateProductUnauthorized();

        visitBrowserContext();

        cy.window().then((window) => {
            return window
                .fetch("/api/products", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(productPayload),
                })
                .then((response) => {
                    expect(response.status).to.eq(401);

                    return response.json();
                })
                .then((body) => {
                    expect(body).to.have.property("status", 401);
                    expect(body).to.have.property("error", "Unauthorized");
                    expect(body).to.have.property(
                        "message",
                        "Authentication is required",
                    );
                    expect(body).to.have.property("path", "/api/products");
                });
        });

        cy.wait("@createProductWithoutAuth").then((interception) => {
            expect(interception.request.method).to.eq("POST");
            expect(interception.response.statusCode).to.eq(401);
        });
    });

    it("should simulate slow response for GET /api/products", () => {
        const delayedProducts = [
            buildMockedProduct({
                id: 9101,
                name: "Delayed Mock Product",
                description: "Product returned after artificial delay",
                price: 399.9,
                stock: 8,
                categoryId: 801,
                categoryName: "Delayed Category",
            }),
        ];

        cy.mockGetProductsWithDelay(delayedProducts, 1500);

        visitBrowserContext();

        cy.window().then((window) => {
            return window
                .fetch("/api/products")
                .then((response) => {
                    expect(response.status).to.eq(200);

                    return response.json();
                })
                .then((body) => {
                    expect(body).to.have.length(1);
                    expect(body[0]).to.have.property(
                        "name",
                        "Delayed Mock Product",
                    );
                });
        });

        cy.wait("@getProductsWithDelay").then((interception) => {
            expect(interception.request.method).to.eq("GET");
            expect(interception.response.statusCode).to.eq(200);
            expect(interception.response.body).to.have.length(1);
        });
    });
});
