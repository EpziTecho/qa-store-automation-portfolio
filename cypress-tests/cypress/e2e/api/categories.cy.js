/*
 * ============================================================
 * File: categories.cy.js
 * Module: Category API Regression Tests
 *
 * Responsibility:
 * Validates the Category API using Cypress API testing.
 *
 * Interaction:
 * Uses custom commands to authenticate as admin, create categories, retrieve
 * categories, update categories, delete categories and validate error scenarios.
 *
 * Design Pattern:
 * API Regression Test Suite.
 *
 * Engineering Principles:
 * - Test independence: tests create their own data.
 * - Dynamic data: factories avoid conflicts with soft-deleted records.
 * - Security validation: protected endpoints require JWT.
 * - Business validation: duplicate category names return 409.
 * - Cleanup: created records are deleted when applicable.
 * ============================================================
 */

import {
    buildUniqueCategoryPayload,
    buildUpdatedCategoryPayload,
} from "../../support/dataFactory";

describe("Category API Regression", () => {
    beforeEach(() => {
        cy.apiLoginAsAdmin().then((loginResponse) => {
            expect(loginResponse.status).to.eq(200);
            expect(loginResponse.body.accessToken).to.not.be.empty;
        });
    });

    it("should get all active categories", () => {
        cy.apiGetAllCategories().then((response) => {
            expect(response.status).to.eq(200);
            expect(response.body).to.be.an("array");
        });
    });

    it("should create a category successfully when admin JWT is valid", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const payload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategory(payload).then((response) => {
                expect(response.status).to.eq(201);

                expect(response.body).to.have.property("id");
                expect(response.body).to.have.property("name", payload.name);
                expect(response.body).to.have.property(
                    "description",
                    payload.description,
                );

                const categoryId = response.body.id;

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                });
            });
        });
    });

    it("should get category by ID after creating it", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const payload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategory(payload).then((createResponse) => {
                expect(createResponse.status).to.eq(201);

                const categoryId = createResponse.body.id;

                cy.apiGetCategoryById(categoryId).then((getResponse) => {
                    expect(getResponse.status).to.eq(200);

                    expect(getResponse.body).to.have.property("id", categoryId);
                    expect(getResponse.body).to.have.property(
                        "name",
                        payload.name,
                    );
                    expect(getResponse.body).to.have.property(
                        "description",
                        payload.description,
                    );
                });

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                });
            });
        });
    });

    it("should update category successfully when admin JWT is valid", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const createPayload = buildUniqueCategoryPayload(baseCategory);
            const updatePayload = buildUpdatedCategoryPayload(baseCategory);

            cy.apiCreateCategory(createPayload).then((createResponse) => {
                expect(createResponse.status).to.eq(201);

                const categoryId = createResponse.body.id;

                cy.apiUpdateCategory(categoryId, updatePayload).then(
                    (updateResponse) => {
                        expect(updateResponse.status).to.eq(200);

                        expect(updateResponse.body).to.have.property(
                            "id",
                            categoryId,
                        );
                        expect(updateResponse.body).to.have.property(
                            "name",
                            updatePayload.name,
                        );
                        expect(updateResponse.body).to.have.property(
                            "description",
                            updatePayload.description,
                        );
                    },
                );

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                });
            });
        });
    });

    it("should return 409 when creating category with duplicated name", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const payload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategory(payload).then((firstResponse) => {
                expect(firstResponse.status).to.eq(201);

                const categoryId = firstResponse.body.id;

                cy.apiCreateCategory(payload).then((duplicateResponse) => {
                    expect(duplicateResponse.status).to.eq(409);

                    expect(duplicateResponse.body).to.have.property(
                        "status",
                        409,
                    );
                    expect(duplicateResponse.body).to.have.property(
                        "error",
                        "Conflict",
                    );
                    expect(duplicateResponse.body).to.have.property("message");
                });

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                });
            });
        });
    });

    it("should return 400 when creating category with invalid payload", () => {
        cy.fixture("categories/invalid-category.json").then(
            (invalidPayload) => {
                cy.apiCreateCategory(invalidPayload).then((response) => {
                    expect(response.status).to.eq(400);

                    expect(response.body).to.have.property("status", 400);
                    expect(response.body).to.have.property(
                        "error",
                        "Bad Request",
                    );
                    expect(response.body).to.have.property(
                        "message",
                        "Request validation failed",
                    );
                    expect(response.body.details).to.be.an("array");
                });
            },
        );
    });

    it("should return 401 when creating category without JWT", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const payload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategoryWithoutToken(payload).then((response) => {
                expect(response.status).to.eq(401);

                expect(response.body).to.have.property("status", 401);
                expect(response.body).to.have.property("error", "Unauthorized");
                expect(response.body).to.have.property(
                    "message",
                    "Authentication is required",
                );
                expect(response.body).to.have.property(
                    "path",
                    "/api/categories",
                );
            });
        });
    });

    it("should soft delete category and return 404 when retrieving it again", () => {
        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const payload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategory(payload).then((createResponse) => {
                expect(createResponse.status).to.eq(201);

                const categoryId = createResponse.body.id;

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                    expect(deleteResponse.body).to.eq("");
                });

                cy.apiGetCategoryById(categoryId).then((getDeletedResponse) => {
                    expect(getDeletedResponse.status).to.eq(404);

                    expect(getDeletedResponse.body).to.have.property(
                        "status",
                        404,
                    );
                    expect(getDeletedResponse.body).to.have.property(
                        "error",
                        "Not Found",
                    );
                    expect(getDeletedResponse.body).to.have.property("message");
                });
            });
        });
    });
});
