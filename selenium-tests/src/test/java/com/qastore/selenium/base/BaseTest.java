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
                Duration.ofSeconds(BrowserConfig.explicitWaitSeconds())
        );
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
        String normalizedPath = path.startsWith("/") ? path : "/" + path;
        driver.get(BrowserConfig.baseUrl() + normalizedPath);
    }
}
