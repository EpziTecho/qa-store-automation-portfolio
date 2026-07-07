# ADR-007: Use Docker Compose for Local Backend and Database Environment

## Status

Accepted

## Context

The QA Store Automation Portfolio requires a reproducible local environment.

Before Docker, the backend depended on:

- A locally installed JDK.
- A locally installed MySQL server.
- Manual database creation.
- Manual environment variable configuration.

This approach works for development but creates inconsistencies across machines and makes onboarding harder.

The project needs an environment that can be started consistently by developers, QA engineers and future CI/CD pipelines.

## Decision

The project will use Docker Compose to orchestrate:

- A Spring Boot API container.
- A MySQL database container.
- A persistent MySQL volume.
- Environment variable based configuration.
- An internal Docker network between services.

The backend will be built using a multi-stage Dockerfile:

- Build stage: Java 17 JDK.
- Runtime stage: Java 17 JRE.

The MySQL container will expose port `3307` on the host to avoid conflicts with local MySQL running on port `3306`.

## Alternatives Considered

### Option 1: Keep using local MySQL only

Rejected.

Local MySQL works, but it requires every machine to install and configure MySQL manually. This makes onboarding and CI/CD preparation harder.

### Option 2: Use Docker only for MySQL

Partially valid, but rejected for the current phase.

Using Docker only for MySQL would reduce database setup problems, but the backend would still depend on the local machine runtime.

### Option 3: Dockerize both backend and MySQL

Accepted.

This gives the project a complete reproducible local environment and prepares it for CI/CD and deployment.

## Consequences

### Positive

- Environment is easier to reproduce.
- MySQL no longer needs to be installed locally.
- Backend and database run in isolated containers.
- Docker Compose provides a single command to start the system.
- CI/CD integration becomes easier.
- The project looks more professional for portfolio review.

### Negative

- Docker Desktop must be installed.
- First build can take time.
- Developers need basic Docker knowledge.
- Database data persists through Docker volumes unless explicitly deleted.
- Environment variables must be managed carefully.

## Implementation Details

Docker files:

```text
backend/qa-store-api/Dockerfile
backend/qa-store-api/.dockerignore
docker/docker-compose.yml
docker/.env.example
docker/.env