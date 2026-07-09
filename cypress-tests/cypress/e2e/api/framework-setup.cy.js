/*
 * ============================================================
 * File: framework-setup.cy.js
 * Module: Cypress Framework Validation
 *
 * Responsibility:
 * Validates that Cypress fixtures, data factories and custom API commands work
 * together correctly.
 *
 * Interaction:
 * Loads category fixture data, generates a unique payload, authenticates as
 * admin, creates a category through the API and deletes it as cleanup.
 *
 * Design Pattern:
 * Framework Smoke Test.
 *
 * Engineering Principles:
 * - Validates framework infrastructure before expanding API regression tests.
 * - Confirms fixture loading.
 * - Confirms dynamic test data generation.
 * - Confirms authenticated custom commands.
 * - Keeps the database clean through cleanup.
 * ============================================================
 */

import { buildUniqueCategoryPayload } from "../../support/dataFactory";

describe("Cypress Framework Setup", () => {
    it("should create and delete a category using fixture and custom commands", () => {
        cy.apiLoginAsAdmin();

        cy.fixture("categories/create-category.json").then((baseCategory) => {
            const categoryPayload = buildUniqueCategoryPayload(baseCategory);

            cy.apiCreateCategory(categoryPayload).then((createResponse) => {
                expect(createResponse.status).to.eq(201);

                expect(createResponse.body).to.have.property("id");
                expect(createResponse.body).to.have.property(
                    "name",
                    categoryPayload.name,
                );
                expect(createResponse.body).to.have.property(
                    "description",
                    categoryPayload.description,
                );

                const categoryId = createResponse.body.id;

                cy.apiGetCategoryById(categoryId).then((getResponse) => {
                    expect(getResponse.status).to.eq(200);
                    expect(getResponse.body).to.have.property("id", categoryId);
                    expect(getResponse.body).to.have.property(
                        "name",
                        categoryPayload.name,
                    );
                });

                cy.apiDeleteCategory(categoryId).then((deleteResponse) => {
                    expect(deleteResponse.status).to.eq(204);
                });

                cy.apiGetCategoryById(categoryId).then((getDeletedResponse) => {
                    expect(getDeletedResponse.status).to.eq(404);
                });
            });
        });
    });
});
