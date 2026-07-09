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
                ExpectedConditions.presenceOfElementLocated(swaggerContainer)
        ));

        return driver.getTitle().contains("Swagger UI")
                || !driver.findElements(swaggerContainer).isEmpty();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
