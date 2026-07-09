/*
 * ============================================================
 * File: BrowserConfig.java
 * Module: Selenium Browser Configuration
 *
 * Responsibility:
 * Reads browser execution settings from Maven/System properties.
 *
 * Interaction:
 * DriverFactory uses this class to decide which browser to start, whether to
 * run in headless mode and which base URL should be tested.
 *
 * Design Pattern:
 * Configuration Provider.
 *
 * Engineering Principles:
 * - Centralized configuration.
 * - Environment-based execution.
 * - Avoids hardcoded values inside tests.
 * - CI/CD-ready design using system properties.
 * - Defensive configuration: blank Maven properties fall back to defaults.
 * ============================================================
 */

package com.qastore.selenium.config;

public final class BrowserConfig {

    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_HEADLESS = "true";
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_EXPLICIT_WAIT_SECONDS = "10";

    private BrowserConfig() {
        /*
         * Utility class.
         * Private constructor prevents object creation.
         */
    }

    public static String browser() {
        return propertyOrDefault("browser", DEFAULT_BROWSER)
                .trim()
                .toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(
                propertyOrDefault("headless", DEFAULT_HEADLESS));
    }

    public static String baseUrl() {
        return propertyOrDefault("base.url", DEFAULT_BASE_URL)
                .trim();
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(
                propertyOrDefault(
                        "explicit.wait.seconds",
                        DEFAULT_EXPLICIT_WAIT_SECONDS));
    }

    private static String propertyOrDefault(String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }
}