/*
 * ============================================================
 * File: interceptRoutes.js
 * Module: Cypress Intercept Route Matchers
 *
 * Responsibility:
 * Centralizes route matchers used by cy.intercept().
 *
 * Interaction:
 * Mocking specs import INTERCEPT_ROUTES to avoid hardcoded intercept patterns.
 *
 * Design Pattern:
 * Route Registry.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated intercept URL patterns.
 * - Maintainability: endpoint changes are updated in one place.
 * - Readability: specs describe network behavior clearly.
 * - Scalability: prepares mocking layer for future UI tests.
 * ============================================================
 */

import { API_ROUTES } from "./apiRoutes";

export const INTERCEPT_ROUTES = Object.freeze({
    health: `**${API_ROUTES.health}`,

    categories: Object.freeze({
        base: `**${API_ROUTES.categories.base}`,
        byId: (categoryId) => `**${API_ROUTES.categories.byId(categoryId)}`,
    }),

    products: Object.freeze({
        base: `**${API_ROUTES.products.base}`,
        byId: (productId) => `**${API_ROUTES.products.byId(productId)}`,
    }),
});
