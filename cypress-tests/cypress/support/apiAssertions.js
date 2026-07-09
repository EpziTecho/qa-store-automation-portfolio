/*
 * ============================================================
 * File: apiAssertions.js
 * Module: Cypress API Assertion Helpers
 *
 * Responsibility:
 * Provides reusable assertion helpers for standard QA Store API responses.
 *
 * Interaction:
 * API spec files import these helpers to validate common success and error
 * response contracts.
 *
 * Design Pattern:
 * Assertion Helper / Test Utility.
 *
 * Engineering Principles:
 * - DRY: avoids repeated assertion blocks across specs.
 * - Consistency: standard API errors are validated the same way everywhere.
 * - Maintainability: error contract changes are updated in one place.
 * - Readability: specs focus on scenario intent instead of low-level checks.
 * ============================================================
 */

/*
 * Validates the common structure of an API error response.
 *
 * Expected backend contract:
 * {
 *   timestamp: "...",
 *   status: 401,
 *   error: "Unauthorized",
 *   message: "Authentication is required",
 *   path: "/api/auth/me",
 *   details: []
 * }
 */
export const assertStandardErrorResponse = (
    response,
    expectedStatus,
    expectedError,
    expectedMessage = null,
    expectedPath = null,
) => {
    expect(response.status).to.eq(expectedStatus);

    expect(response.body).to.have.property("timestamp");
    expect(response.body).to.have.property("status", expectedStatus);
    expect(response.body).to.have.property("error", expectedError);
    expect(response.body).to.have.property("message");
    expect(response.body).to.have.property("path");
    expect(response.body).to.have.property("details");

    expect(response.body.details).to.be.an("array");

    if (expectedMessage) {
        expect(response.body.message).to.eq(expectedMessage);
    }

    if (expectedPath) {
        expect(response.body.path).to.eq(expectedPath);
    }
};

/*
 * Validates 400 Bad Request responses caused by Bean Validation failures.
 */
export const assertBadRequestValidationError = (
    response,
    expectedPath = null,
) => {
    assertStandardErrorResponse(
        response,
        400,
        "Bad Request",
        "Request validation failed",
        expectedPath,
    );

    expect(response.body.details).to.be.an("array");
};

/*
 * Validates 401 Unauthorized responses.
 *
 * Used when a request is missing authentication or has an invalid token.
 */
export const assertUnauthorizedError = (
    response,
    expectedPath = null,
    expectedMessage = "Authentication is required",
) => {
    assertStandardErrorResponse(
        response,
        401,
        "Unauthorized",
        expectedMessage,
        expectedPath,
    );
};

/*
 * Validates 401 Unauthorized responses for invalid login credentials.
 */
export const assertInvalidCredentialsError = (response) => {
    assertStandardErrorResponse(
        response,
        401,
        "Unauthorized",
        "Invalid email or password",
        "/api/auth/login",
    );
};

/*
 * Validates 403 Forbidden responses.
 *
 * Used when a user is authenticated but does not have the required authority.
 */
export const assertForbiddenError = (
    response,
    expectedPath = null,
    expectedMessage = "Access is denied",
) => {
    assertStandardErrorResponse(
        response,
        403,
        "Forbidden",
        expectedMessage,
        expectedPath,
    );
};

/*
 * Validates 404 Not Found responses.
 */
export const assertNotFoundError = (response, expectedPath = null) => {
    assertStandardErrorResponse(response, 404, "Not Found", null, expectedPath);
};

/*
 * Validates 409 Conflict responses.
 *
 * Used for business rule violations such as duplicated category names.
 */
export const assertConflictError = (response, expectedPath = null) => {
    assertStandardErrorResponse(response, 409, "Conflict", null, expectedPath);
};

/*
 * Validates 204 No Content responses.
 *
 * DELETE endpoints should return no body.
 */
export const assertNoContentResponse = (response) => {
    expect(response.status).to.eq(204);
    expect(response.body).to.eq("");
};
