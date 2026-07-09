# Selenium + TestNG Guide - QA Store Automation Portfolio

## 1. Purpose

This document explains the Selenium + TestNG module used in the QA Store Automation Portfolio.

The purpose of this phase is to introduce Java-based browser automation using Selenium WebDriver, TestNG, WebDriverManager and Page Object Model.

---

## 2. Why Selenium?

Selenium is a browser automation tool commonly used for UI testing.

In this project, Selenium complements Cypress:

- Cypress is used for API automation, mocking and future UI tests.
- Selenium is used to demonstrate Java-based UI automation skills.
- Selenium + TestNG is common in enterprise QA Automation roles.

---

## 3. Current Scope

The current Selenium suite validates:

```text
Swagger UI loads successfully in a real browser.

This is the correct first smoke test because the backend already exposes Swagger UI and the project does not yet include a custom frontend.

4. Architecture
TestNG Suite
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