# ADR-004: Use MySQL, JPA and Soft Delete for Persistence

## Status

Accepted

## Context

The QA Store API requires persistent data storage to support realistic backend and QA Automation scenarios.

The project needs to demonstrate:

- REST API development.
- Database persistence.
- Entity relationships.
- API testing.
- Database validation.
- CI/CD readiness.
- Docker compatibility.
- Professional backend architecture.

Initially, products were stored in memory to validate the API structure quickly. However, an in-memory implementation does not represent real enterprise behavior and does not allow meaningful database validation.

## Decision

The project will use:

- MySQL as the relational database.
- Spring Data JPA with Hibernate as the persistence layer.
- Repository Pattern for database access.
- Soft delete using an `active` field.
- A `ManyToOne` relationship between Product and Category.

Current relationship:

```text
Category 1 ───── N Product