/*
 * ============================================================
 * File: products.cy.js
 * Module: Product API Regression Tests
 *
 * Responsibility:
 * Validates the Product API using Cypress API testing.
 *
 * Interaction:
 * Uses custom commands to authenticate as admin, create supporting categories,
 * create products, retrieve products, update products, delete products and
 * validate error scenarios.
 *
 * Design Pattern:
 * API Regression Test Suite.
 *
 * Engineering Principles:
 * - Test independence: each test creates its own data.
 * - Relationship validation: products are created with real active categories.
 * - Dynamic data: factories avoid conflicts with existing records.
 * - Security validation: protected endpoints require JWT.
 * - Cleanup: products are deleted before categories to respect business rules.
 * ============================================================
 */

import {
    buildUniqueCategoryPayload,
    buildUniqueProductPayload,
    buildUpdatedProductPayload,
} from "../../support/dataFactory";

describe("Product API Regression", () => {
    beforeEach(() => {
        cy.apiLoginAsAdmin().then((loginResponse) => {
            expect(loginResponse.status).to.eq(200);
            expect(loginResponse.body.accessToken).to.not.be.empty;
        });
    });

    /*
     * Helper used by product tests.
     *
     * It creates a dedicated category for the test and returns its response.
     * This avoids using hardcoded category IDs.
     */
    const createCategoryForProductTest = () => {
        return cy
            .fixture("categories/create-category.json")
            .then((baseCategory) => {
                const categoryPayload =
                    buildUniqueCategoryPayload(baseCategory);

                return cy
                    .apiCreateCategory(categoryPayload)
                    .then((categoryResponse) => {
                        expect(categoryResponse.status).to.eq(201);

                        return categoryResponse.body;
                    });
            });
    };

    /*
     * Cleanup helper.
     *
     * Product must be deleted before category because the backend blocks category
     * deletion when active products are associated with it.
     */
    const deleteProductAndCategory = (productId, categoryId) => {
        cy.apiDeleteProduct(productId).then((deleteProductResponse) => {
            expect(deleteProductResponse.status).to.eq(204);
        });

        cy.apiDeleteCategory(categoryId).then((deleteCategoryResponse) => {
            expect(deleteCategoryResponse.status).to.eq(204);
        });
    };

    it("should get all active products", () => {
        cy.apiGetAllProducts().then((response) => {
            expect(response.status).to.eq(200);
            expect(response.body).to.be.an("array");
        });
    });

    it("should create a product successfully when admin JWT and category are valid", () => {
        createCategoryForProductTest().then((category) => {
            cy.fixture("products/create-product.json").then((baseProduct) => {
                const productPayload = buildUniqueProductPayload(
                    baseProduct,
                    category.id,
                );

                cy.apiCreateProduct(productPayload).then((response) => {
                    expect(response.status).to.eq(201);

                    expect(response.body).to.have.property("id");
                    expect(response.body).to.have.property(
                        "name",
                        productPayload.name,
                    );
                    expect(response.body).to.have.property(
                        "description",
                        productPayload.description,
                    );
                    expect(Number(response.body.price)).to.eq(
                        productPayload.price,
                    );
                    expect(response.body).to.have.property(
                        "stock",
                        productPayload.stock,
                    );
                    expect(response.body).to.have.property(
                        "categoryId",
                        category.id,
                    );
                    expect(response.body).to.have.property(
                        "categoryName",
                        category.name,
                    );

                    deleteProductAndCategory(response.body.id, category.id);
                });
            });
        });
    });

    it("should get product by ID after creating it", () => {
        createCategoryForProductTest().then((category) => {
            cy.fixture("products/create-product.json").then((baseProduct) => {
                const productPayload = buildUniqueProductPayload(
                    baseProduct,
                    category.id,
                );

                cy.apiCreateProduct(productPayload).then((createResponse) => {
                    expect(createResponse.status).to.eq(201);

                    const productId = createResponse.body.id;

                    cy.apiGetProductById(productId).then((getResponse) => {
                        expect(getResponse.status).to.eq(200);

                        expect(getResponse.body).to.have.property(
                            "id",
                            productId,
                        );
                        expect(getResponse.body).to.have.property(
                            "name",
                            productPayload.name,
                        );
                        expect(getResponse.body).to.have.property(
                            "description",
                            productPayload.description,
                        );
                        expect(getResponse.body).to.have.property(
                            "categoryId",
                            category.id,
                        );
                        expect(getResponse.body).to.have.property(
                            "categoryName",
                            category.name,
                        );
                    });

                    deleteProductAndCategory(productId, category.id);
                });
            });
        });
    });

    it("should update product successfully when admin JWT is valid", () => {
        createCategoryForProductTest().then((category) => {
            cy.fixture("products/create-product.json").then((baseProduct) => {
                const createPayload = buildUniqueProductPayload(
                    baseProduct,
                    category.id,
                );
                const updatePayload = buildUpdatedProductPayload(
                    baseProduct,
                    category.id,
                );

                cy.apiCreateProduct(createPayload).then((createResponse) => {
                    expect(createResponse.status).to.eq(201);

                    const productId = createResponse.body.id;

                    cy.apiUpdateProduct(productId, updatePayload).then(
                        (updateResponse) => {
                            expect(updateResponse.status).to.eq(200);

                            expect(updateResponse.body).to.have.property(
                                "id",
                                productId,
                            );
                            expect(updateResponse.body).to.have.property(
                                "name",
                                updatePayload.name,
                            );
                            expect(updateResponse.body).to.have.property(
                                "description",
                                updatePayload.description,
                            );
                            expect(Number(updateResponse.body.price)).to.eq(
                                updatePayload.price,
                            );
                            expect(updateResponse.body).to.have.property(
                                "stock",
                                updatePayload.stock,
                            );
                            expect(updateResponse.body).to.have.property(
                                "categoryId",
                                category.id,
                            );
                            expect(updateResponse.body).to.have.property(
                                "categoryName",
                                category.name,
                            );
                        },
                    );

                    deleteProductAndCategory(productId, category.id);
                });
            });
        });
    });

    it("should return 400 when creating product with invalid payload", () => {
        cy.fixture("products/invalid-product.json").then((invalidPayload) => {
            cy.apiCreateProduct(invalidPayload).then((response) => {
                expect(response.status).to.eq(400);

                expect(response.body).to.have.property("status", 400);
                expect(response.body).to.have.property("error", "Bad Request");
                expect(response.body).to.have.property(
                    "message",
                    "Request validation failed",
                );
                expect(response.body.details).to.be.an("array");
            });
        });
    });

    it("should return 404 when creating product with non-existing category", () => {
        cy.fixture("products/create-product.json").then((baseProduct) => {
            const productPayload = buildUniqueProductPayload(
                baseProduct,
                99999999,
            );

            cy.apiCreateProduct(productPayload).then((response) => {
                expect(response.status).to.eq(404);

                expect(response.body).to.have.property("status", 404);
                expect(response.body).to.have.property("error", "Not Found");
                expect(response.body).to.have.property("message");
            });
        });
    });

    it("should return 401 when creating product without JWT", () => {
        createCategoryForProductTest().then((category) => {
            cy.fixture("products/create-product.json").then((baseProduct) => {
                const productPayload = buildUniqueProductPayload(
                    baseProduct,
                    category.id,
                );

                cy.apiCreateProductWithoutToken(productPayload).then(
                    (response) => {
                        expect(response.status).to.eq(401);

                        expect(response.body).to.have.property("status", 401);
                        expect(response.body).to.have.property(
                            "error",
                            "Unauthorized",
                        );
                        expect(response.body).to.have.property(
                            "message",
                            "Authentication is required",
                        );
                        expect(response.body).to.have.property(
                            "path",
                            "/api/products",
                        );
                    },
                );

                cy.apiDeleteCategory(category.id).then(
                    (deleteCategoryResponse) => {
                        expect(deleteCategoryResponse.status).to.eq(204);
                    },
                );
            });
        });
    });

    it("should soft delete product and return 404 when retrieving it again", () => {
        createCategoryForProductTest().then((category) => {
            cy.fixture("products/create-product.json").then((baseProduct) => {
                const productPayload = buildUniqueProductPayload(
                    baseProduct,
                    category.id,
                );

                cy.apiCreateProduct(productPayload).then((createResponse) => {
                    expect(createResponse.status).to.eq(201);

                    const productId = createResponse.body.id;

                    cy.apiDeleteProduct(productId).then(
                        (deleteProductResponse) => {
                            expect(deleteProductResponse.status).to.eq(204);
                            expect(deleteProductResponse.body).to.eq("");
                        },
                    );

                    cy.apiGetProductById(productId).then(
                        (getDeletedResponse) => {
                            expect(getDeletedResponse.status).to.eq(404);

                            expect(getDeletedResponse.body).to.have.property(
                                "status",
                                404,
                            );
                            expect(getDeletedResponse.body).to.have.property(
                                "error",
                                "Not Found",
                            );
                            expect(getDeletedResponse.body).to.have.property(
                                "message",
                            );
                        },
                    );

                    cy.apiDeleteCategory(category.id).then(
                        (deleteCategoryResponse) => {
                            expect(deleteCategoryResponse.status).to.eq(204);
                        },
                    );
                });
            });
        });
    });
});
