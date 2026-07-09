/*
 * ============================================================
 * File: BaseTest.java
 * Module: Selenium Base Test
 *
 * Responsibility:
 * Provides common WebDriver setup and teardown logic for all Selenium tests.
 *
 * Interaction:
 * Test classes extend BaseTest to get access to WebDriver, WebDriverWait and
 * navigation helpers.
 *
 * Design Pattern:
 * Template Method.
 *
 * Engineering Principles:
 * - DRY: avoids repeating setup and cleanup in every test.
 * - Resource safety: browser is always closed after each test.
 * - Test isolation: each test starts with a fresh WebDriver instance.
 * - Maintainability: common test lifecycle logic is centralized.
 * ============================================================
 */

package com.qastore.selenium.base;

import com.qastore.selenium.config.BrowserConfig;
import com.qastore.selenium.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public abstract class BaseTest {

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();

        webDriverWait = new WebDriverWait(
                driver,
                Duration.ofSeconds(BrowserConfig.explicitWaitSeconds()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected WebDriver driver() {
        return driver;
    }

    protected WebDriverWait webDriverWait() {
        return webDriverWait;
    }

    protected void openPath(String path) {
        String normalizedPath = path.startsWith("/")
                ? path
                : "/" + path;

        driver.get(BrowserConfig.baseUrl() + normalizedPath);
    }
}