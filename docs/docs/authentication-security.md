# Authentication and Authorization - QA Store API

## 1. Purpose

This document describes the authentication and authorization strategy used by the QA Store API.

The project uses JWT-based stateless authentication with Spring Security.

The goal is to simulate a professional backend security model that can be tested through API testing tools such as Swagger UI, Postman, Cypress API tests and REST Assured.

---

## 2. Security Model

The QA Store API uses:

- Spring Security.
- BCrypt password hashing.
- JWT access tokens.
- Role-based authorization.
- Stateless session management.
- JSON error responses for authentication and authorization failures.

---

## 3. Main Security Flow

### Login Flow

```text
POST /api/auth/login
        |
        v
AuthController
        |
        v
AuthService
        |
        |-- Find user by email
        |-- Validate password with BCrypt
        |-- Validate active user
        |-- Generate JWT
        v
LoginResponse