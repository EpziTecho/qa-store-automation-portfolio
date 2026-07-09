/*
 * ============================================================
 * File: network-intercept.cy.js
 * Module: Cypress Mocking and Network Intercept Tests
 *
 * Responsibility:
 * Demonstrates how to use cy.intercept() to spy, mock and simulate API
 * responses from the browser network layer.
 *
 * Interaction:
 * Uses cy.intercept() to control requests made from the browser context.
 * The backend may be running, but mocked requests do not depend on real
 * database data.
 *
 * Design Pattern:
 * Network Stub / Test Double.
 *
 * Engineering Principles:
 * - Controlled testing: validates frontend/API behavior with deterministic responses.
 * - Failure simulation: tests error scenarios without forcing backend failures.
 * - Isolation: mocks reduce dependency on database state for selected scenarios.
 * - Observability: aliases allow assertions on intercepted requests and responses.
 * ============================================================
 */

describe("Network Mocking with cy.intercept", () => {
    /*
     * We visit Swagger UI only to have a valid same-origin browser page.
     *
     * The test itself does not depend on Swagger behavior.
     * After the page loads, we execute fetch() from the browser context.
     */
    const visitBrowserContext = () => {
        cy.visit("/swagger-ui.html");
    };

    it("should mock GET /api/products response using fixture data", () => {
        cy.fixture("products/mocked-products.json").then((mockedProducts) => {
            cy.intercept("GET", "**/api/products", {
                statusCode: 200,
                body: mockedProducts,
            }).as("getMockedProducts");

            visitBrowserContext();

            cy.window().then((window) => {
                return window
                    .fetch("/api/products")
                    .then((response) => response.json())
                    .then((body) => {
                        expect(body).to.have.length(2);
                        expect(body[0]).to.have.property("id", 1001);
                        expect(body[0]).to.have.property(
                            "name",
                            "Mocked Cypress Laptop",
                        );
                        expect(body[0]).to.have.property(
                            "categoryName",
                            "Mocked Electronics",
                        );
                    });
            });

            cy.wait("@getMockedProducts").then((interception) => {
                expect(interception.request.method).to.eq("GET");
                expect(interception.response.statusCode).to.eq(200);
                expect(interception.response.body).to.have.length(2);
            });
        });
    });

    it("should simulate 500 Internal Server Error for GET /api/categories", () => {
        cy.intercept("GET", "**/api/categories", {
            statusCode: 500,
            body: {
                timestamp: new Date().toISOString(),
                status: 500,
                error: "Internal Server Error",
                message: "Simulated server failure from Cypress intercept",
                path: "/api/categories",
                details: [],
            },
        }).as("getCategoriesFailure");

        visitBrowserContext();

        cy.window().then((window) => {
            return window
                .fetch("/api/categories")
                .then((response) => {
                    expect(response.status).to.eq(500);

                    return response.json();
                })
                .then((body) => {
                    expect(body).to.have.property("status", 500);
                    expect(body).to.have.property(
                        "error",
                        "Internal Server Error",
                    );
                    expect(body).to.have.property(
                        "message",
                        "Simulated server failure from Cypress intercept",
                    );
                });
        });

        cy.wait("@getCategoriesFailure").then((interception) => {
            expect(interception.request.method).to.eq("GET");
            expect(interception.response.statusCode).to.eq(500);
        });
    });

    it("should spy on real GET /api/health request without modifying the response", () => {
        cy.intercept("GET", "**/api/health").as("getHealth");

        visitBrowserContext();

        cy.window().then((window) => {
            return window
                .fetch("/api/health")
                .then((response) => response.json())
                .then((body) => {
                    expect(body).to.have.property("status", "UP");
                    expect(body).to.have.property("service", "qa-store-api");
                });
        });

        cy.wait("@getHealth").then((interception) => {
            expect(interception.request.method).to.eq("GET");
            expect(interception.response.statusCode).to.eq(200);
        });
    });
});
