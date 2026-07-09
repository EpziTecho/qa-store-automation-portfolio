/*
 * ============================================================
 * File: dataFactory.js
 * Module: Cypress Test Data Factory
 *
 * Responsibility:
 * Provides reusable functions to generate dynamic test data for API tests.
 *
 * Interaction:
 * Specs and custom commands can use these factory functions to build request
 * payloads based on fixtures while avoiding duplicated names in the database.
 *
 * Design Pattern:
 * Factory Pattern.
 *
 * Engineering Principles:
 * - DRY: centralizes test data generation.
 * - Maintainability: payload changes are handled in one place.
 * - Test isolation: unique names reduce conflicts with soft-deleted records.
 * - Reusability: same factory can be used across multiple specs.
 * ============================================================
 */

/*
 * Generates a unique suffix using timestamp and a random number.
 *
 * This is useful because the backend applies uniqueness rules to category names.
 * Since soft-deleted records still exist in the database, repeated static names
 * could cause 409 Conflict responses.
 */
export const uniqueSuffix = () => {
    return `${Date.now()}-${Math.floor(Math.random() * 100000)}`;
};

/*
 * Builds a category payload using a base fixture.
 *
 * Example:
 * Base fixture name: Cypress Category
 * Generated name: Cypress Category 1760000000000-12345
 */
export const buildUniqueCategoryPayload = (baseCategory) => {
    return {
        ...baseCategory,
        name: `${baseCategory.name} ${uniqueSuffix()}`,
    };
};

/*
 * Builds a product payload using a base fixture and a dynamic category ID.
 *
 * Products require an existing active category.
 */
export const buildUniqueProductPayload = (baseProduct, categoryId) => {
    return {
        ...baseProduct,
        name: `${baseProduct.name} ${uniqueSuffix()}`,
        categoryId,
    };
};
