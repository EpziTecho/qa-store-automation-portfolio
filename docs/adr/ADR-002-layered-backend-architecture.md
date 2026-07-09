# ADR-002: Use Layered Architecture for the Backend

## Status

Accepted

## Context

The backend of the QA Store Automation Portfolio will be developed using Spring Boot.

The backend must expose REST APIs, apply business rules, persist data in MySQL and support automated testing.

A backend architecture style was required before implementation.

The main alternatives were:

1. Simple controller-only architecture.
2. Layered architecture.
3. Hexagonal architecture.
4. Clean architecture.

## Decision

Use a layered architecture.

The backend will be organized into the following layers:

```text
Controller Layer
      |
Service Layer
      |
Repository Layer
      |
Database Layer