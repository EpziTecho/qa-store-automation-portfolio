/*
 * ============================================================
 * File: request-validation.cy.js
 * Module: Cypress Request Validation with cy.intercept
 *
 * Responsibility:
 * Demonstrates how to validate outgoing HTTP requests using cy.intercept().
 *
 * Interaction:
 * Executes browser-level fetch() calls from a valid application page and
 * intercepts those requests before they reach the backend.
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

describe("Request validation with cy.intercept", () => {
    /*
     * We visit Swagger UI only to create a valid browser context.
     *
     * The test does not validate Swagger itself.
     * The network request is executed manually through window.fetch().
     */
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

        const mockedResponse = {
            id: 9001,
            name: productPayload.name,
            description: productPayload.description,
            price: productPayload.price,
            stock: productPayload.stock,
            categoryId: productPayload.categoryId,
            categoryName: "Intercept Category",
        };

        cy.intercept("POST", "**/api/products", (req) => {
            /*
             * These assertions validate what the browser/client is sending.
             *
             * In a real UI test, this is how we verify that the frontend sends the
             * correct payload after a user completes a form.
             */
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
        }).as("createProduct");

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
                    expect(body).to.deep.eq(mockedResponse);
                });
        });

        cy.wait("@createProduct").then((interception) => {
            expect(interception.request.method).to.eq("POST");
            expect(interception.request.body).to.deep.eq(productPayload);
            expect(interception.response.statusCode).to.eq(201);
            expect(interception.response.body).to.deep.eq(mockedResponse);
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

        cy.intercept("POST", "**/api/products", (req) => {
            expect(req.method).to.eq("POST");
            expect(req.headers).to.not.have.property("authorization");

            req.reply({
                statusCode: 401,
                body: {
                    timestamp: new Date().toISOString(),
                    status: 401,
                    error: "Unauthorized",
                    message: "Authentication is required",
                    path: "/api/products",
                    details: [],
                },
            });
        }).as("createProductWithoutAuth");

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
            {
                id: 9101,
                name: "Delayed Mock Product",
                description: "Product returned after artificial delay",
                price: 399.9,
                stock: 8,
                categoryId: 801,
                categoryName: "Delayed Category",
            },
        ];

        cy.intercept("GET", "**/api/products", {
            statusCode: 200,
            delay: 1500,
            body: delayedProducts,
        }).as("getProductsWithDelay");

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
