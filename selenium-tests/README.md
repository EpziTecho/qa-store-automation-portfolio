# Selenium + TestNG Framework

## Purpose

This module contains the Selenium WebDriver + TestNG automation framework for the QA Store Automation Portfolio.

The goal is to provide a Java-based UI automation layer separated from:

- Spring Boot backend tests.
- Cypress API and mocking tests.
- Postman collections.
- Future REST Assured tests.

---

## Current Scope

The current Selenium framework validates that Swagger UI is accessible through a real browser session.

This is used as the first UI smoke test because the project does not yet include a custom frontend.

---

## Architecture

```text
TestNG
  |
  v
SwaggerSmokeTest
  |
  v
BaseTest
  |
  v
DriverFactory
  |
  v
ChromeDriver
  |
  v
SwaggerHomePage


Structure
selenium-tests/
├── pom.xml
├── testng.xml
├── README.md
└── src/
    └── test/
        └── java/
            └── com/qastore/selenium/
                ├── base/
                │   └── BaseTest.java
                ├── config/
                │   └── BrowserConfig.java
                ├── driver/
                │   └── DriverFactory.java
                ├── pages/
                │   └── SwaggerHomePage.java
                └── tests/
                    └── SwaggerSmokeTest.java
Main Components
BrowserConfig

Reads runtime configuration from system properties.

Supported properties:

browser
headless
base.url
explicit.wait.seconds
DriverFactory

Creates WebDriver instances.

Currently supported browser:

chrome
BaseTest

Provides common setup and teardown logic.

Each test gets a fresh browser session.

SwaggerHomePage

Page Object representing Swagger UI.

SwaggerSmokeTest

Smoke test that validates Swagger UI loads successfully in a browser.

Running Tests

Start the backend first:

cd ../docker
docker compose --env-file .env -f docker-compose.yml up --build

Run Selenium tests:

cd ..
.\backend\qa-store-api\mvnw.cmd -f selenium-tests\pom.xml clean test -Dbrowser=chrome

Run with visible browser:

.\backend\qa-store-api\mvnw.cmd -f selenium-tests\pom.xml clean test -Dbrowser=chrome -Dheadless=false

Override base URL:

.\backend\qa-store-api\mvnw.cmd -f selenium-tests\pom.xml clean test -Dbase.url=http://localhost:8080
Design Decisions
Selenium tests live in their own Maven module.
TestNG is used for Java test lifecycle and suite execution.
WebDriverManager manages browser drivers automatically.
Page Object Model separates page behavior from test logic.
Headless mode is enabled by default for future CI/CD execution.
Current Expected Result
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Future Improvements
Add more Page Objects when a frontend exists.
Add browser screenshots on failure.
Add parallel execution.
Add Selenium reports.
Add GitHub Actions execution.
Add cross-browser support.