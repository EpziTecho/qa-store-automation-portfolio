/*
 * ============================================================
 * File: apiRoutes.js
 * Module: Cypress API Route Constants
 *
 * Responsibility:
 * Centralizes all QA Store API endpoint paths used by Cypress tests.
 *
 * Interaction:
 * Custom commands import API_ROUTES to avoid hardcoding endpoint strings.
 * Spec files indirectly use these routes through custom commands.
 *
 * Design Pattern:
 * Constants / Route Registry.
 *
 * Engineering Principles:
 * - DRY: avoids duplicated endpoint strings.
 * - Maintainability: endpoint changes are updated in one place.
 * - Readability: commands express API intent more clearly.
 * - Scalability: prepares the framework for future modules and versioned APIs.
 * ============================================================
 */

export const API_ROUTES = Object.freeze({
    health: "/api/health",

    auth: Object.freeze({
        login: "/api/auth/login",
        me: "/api/auth/me",
    }),

    categories: Object.freeze({
        base: "/api/categories",
        byId: (categoryId) => `/api/categories/${categoryId}`,
    }),

    products: Object.freeze({
        base: "/api/products",
        byId: (productId) => `/api/products/${productId}`,
    }),
});
