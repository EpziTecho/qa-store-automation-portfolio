package com.qastore.selenium.config;

public final class BrowserConfig {

    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_HEADLESS = "true";
    private static final String DEFAULT_BASE_URL = "http://localhost:8080";
    private static final String DEFAULT_EXPLICIT_WAIT_SECONDS = "10";

    private BrowserConfig() {
    }

    public static String browser() {
        return propertyOrDefault("browser", DEFAULT_BROWSER).trim().toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(propertyOrDefault("headless", DEFAULT_HEADLESS));
    }

    public static String baseUrl() {
        return propertyOrDefault("base.url", DEFAULT_BASE_URL).trim();
    }

    public static int explicitWaitSeconds() {
        return Integer.parseInt(
                propertyOrDefault("explicit.wait.seconds", DEFAULT_EXPLICIT_WAIT_SECONDS)
        );
    }

    private static String propertyOrDefault(String propertyName, String defaultValue) {
        String value = System.getProperty(propertyName);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    }
}
