/*
 * ============================================================
 * File: SwaggerHomePage.java
 * Module: Swagger UI Page Object
 *
 * Responsibility:
 * Represents the Swagger UI page exposed by the Spring Boot backend.
 *
 * Interaction:
 * Selenium tests use this Page Object to validate that Swagger UI is available
 * through the browser.
 *
 * Design Pattern:
 * Page Object Model.
 *
 * Engineering Principles:
 * - Separation of concerns: locators and UI behavior are not placed in tests.
 * - Maintainability: UI locator changes are updated in one class.
 * - Readability: tests express intent instead of Selenium implementation details.
 * - Reusability: the page can be reused by future Selenium tests.
 * ============================================================
 */

package com.qastore.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SwaggerHomePage {

    private final WebDriver driver;
    private final WebDriverWait webDriverWait;

    private final By swaggerContainer = By.cssSelector(".swagger-ui");

    public SwaggerHomePage(WebDriver driver, WebDriverWait webDriverWait) {
        this.driver = driver;
        this.webDriverWait = webDriverWait;
    }

    public boolean isLoaded() {
        webDriverWait.until(ExpectedConditions.or(
                ExpectedConditions.titleContains("Swagger UI"),
                ExpectedConditions.presenceOfElementLocated(swaggerContainer)));

        return driver.getTitle().contains("Swagger UI")
                || !driver.findElements(swaggerContainer).isEmpty();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}