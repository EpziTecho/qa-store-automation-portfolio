/*
 * ============================================================
 * File: SwaggerSmokeTest.java
 * Module: Selenium Smoke Tests
 *
 * Responsibility:
 * Validates that Swagger UI is accessible through a real browser session.
 *
 * Interaction:
 * Extends BaseTest to reuse WebDriver lifecycle management.
 * Uses SwaggerHomePage to interact with the Swagger UI page.
 *
 * Design Pattern:
 * Smoke Test + Page Object Model.
 *
 * Engineering Principles:
 * - Fast feedback: validates browser-to-backend availability.
 * - Test isolation: browser is created and closed for each test.
 * - Maintainability: UI checks are delegated to a Page Object.
 * - Portfolio readiness: demonstrates Selenium + TestNG foundation.
 * ============================================================
 */

package com.qastore.selenium.tests;

import com.qastore.selenium.base.BaseTest;
import com.qastore.selenium.pages.SwaggerHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SwaggerSmokeTest extends BaseTest {

        @Test(description = "Swagger UI should load successfully in the browser")
        public void shouldLoadSwaggerUiSuccessfully() {
                openPath("/swagger-ui.html");

                SwaggerHomePage swaggerHomePage = new SwaggerHomePage(
                                driver(),
                                webDriverWait());

                Assert.assertTrue(
                                swaggerHomePage.isLoaded(),
                                "Swagger UI should be loaded successfully.");

                Assert.assertTrue(
                                swaggerHomePage.currentUrl().contains("/swagger-ui"),
                                "Current URL should contain /swagger-ui.");
        }
}