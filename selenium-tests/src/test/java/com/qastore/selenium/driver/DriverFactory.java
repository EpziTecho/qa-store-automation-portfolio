/*
 * ============================================================
 * File: DriverFactory.java
 * Module: Selenium WebDriver Factory
 *
 * Responsibility:
 * Creates configured WebDriver instances for supported browsers.
 *
 * Interaction:
 * BaseTest calls DriverFactory.createDriver() before each test method.
 * BrowserConfig provides runtime configuration such as browser and headless mode.
 *
 * Design Pattern:
 * Factory Method.
 *
 * Engineering Principles:
 * - Single Responsibility: only creates WebDriver instances.
 * - Open/Closed Principle: new browsers can be added without modifying tests.
 * - DRY: browser setup is centralized.
 * - CI/CD-ready: supports headless execution by default.
 * ============================================================
 */

package com.qastore.selenium.driver;

import com.qastore.selenium.config.BrowserConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {

    private DriverFactory() {
        /*
         * Utility class.
         * Prevents instantiation because this class only exposes factory methods.
         */
    }

    public static WebDriver createDriver() {
        String browser = BrowserConfig.browser();

        return switch (browser) {
            case "chrome" -> createChromeDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (BrowserConfig.headless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }
}