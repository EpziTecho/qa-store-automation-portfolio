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
                webDriverWait()
        );

        Assert.assertTrue(
                swaggerHomePage.isLoaded(),
                "Swagger UI should be loaded successfully."
        );

        Assert.assertTrue(
                swaggerHomePage.currentUrl().contains("/swagger-ui"),
                "Current URL should contain /swagger-ui."
        );
    }
}
