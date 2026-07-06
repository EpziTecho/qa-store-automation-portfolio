# Project Architecture

## Responsibility

This document defines the general architecture of the QA Store Automation Portfolio.

It explains how the backend, database, test automation frameworks, Docker environment and CI/CD pipelines interact with each other.

## Architectural Goal

Build a professional QA Automation ecosystem that can be used as technical evidence in national and international software engineering recruitment processes.

## Project Domain

The project simulates a small e-commerce platform named **QA Store**.

The domain includes:

- Authentication
- Users
- Roles
- Products
- Categories
- Orders
- Order items

This domain was selected because it allows realistic QA scenarios such as:

- CRUD testing
- Authentication testing
- Authorization testing
- API testing
- UI testing
- Database validation
- Mocking
- Regression testing
- CI/CD validation

## Logical Architecture

```text
GitHub Repository
      |
      v
GitHub Actions CI/CD
      |
      v
Docker Environment
      |
      v
Spring Boot Backend
      |
      v
MySQL Database
```

## Testing Architecture

```text
Postman
  |
  |-- API exploration
  |-- Manual API validation
  |-- Collection-based testing

REST Assured
  |
  |-- Java-based API automation
  |-- Regression API testing
  |-- CI/CD execution

Cypress
  |
  |-- UI automation
  |-- API testing
  |-- Mocking with cy.intercept()

Selenium + TestNG
  |
  |-- Enterprise-style UI automation
  |-- Page Object Model
  |-- Java-based test framework
```

## Backend Architecture Style

The backend will use a layered architecture.

```text
Controller Layer
      |
      v
Service Layer
      |
      v
Repository Layer
      |
      v
Database Layer
```

## Layer Responsibilities

| Layer | Responsibility |
|---|---|
| Controller | Exposes REST endpoints and handles HTTP requests |
| Service | Contains business logic and coordinates operations |
| Repository | Handles database access using Spring Data JPA |
| Database | Stores persistent data in MySQL |

## Repository Structure

```text
qa-store-automation-portfolio/
│
├── backend/
├── cypress-tests/
├── selenium-tests/
├── rest-assured-tests/
├── postman/
├── docker/
├── docs/
├── .github/workflows/
├── README.md
└── .gitignore
```

## Main Engineering Principles

The project will apply the following engineering principles:

- Separation of concerns
- Single Responsibility Principle
- DRY
- KISS
- Clean Code
- Testability
- Maintainability
- Modular organization
- Professional documentation