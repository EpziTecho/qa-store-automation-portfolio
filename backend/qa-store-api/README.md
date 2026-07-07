# QA Store API

Spring Boot REST API used as the backend system under test for the QA Store Automation Portfolio.

## Responsibility

This module provides the backend API that will be tested by:

- Postman
- REST Assured
- Cypress
- Selenium + TestNG
- GitHub Actions
- Docker-based environments

## Current Scope

The current backend version includes:

- Health check endpoint
- Product module
- Request validation
- Global exception handling
- In-memory product storage
- Automated tests with JUnit and MockMvc

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot | Backend framework |
| Maven | Build and dependency management |
| Spring Web | REST API development |
| Bean Validation | Request validation |
| JUnit 5 | Automated testing |
| MockMvc | HTTP layer testing without real server |

## Architecture

The backend currently follows a layered architecture:

```text
HTTP Request
     |
     v
Controller Layer
     |
     v
Service Layer
     |
     v
In-Memory Storage
```

In the next phase, in-memory storage will be replaced by:

```text
Repository Layer
     |
     v
MySQL Database
```

## Current Endpoints

### Health Check

```http
GET /api/health
```

Expected response:

```json
{
  "status": "UP",
  "service": "qa-store-api",
  "version": "1.0.0",
  "message": "QA Store API is running"
}
```

### Products

```http
GET /api/products
GET /api/products/{id}
POST /api/products
```

Example request:

```http
POST /api/products
Content-Type: application/json
```

```json
{
  "name": "Mechanical Keyboard",
  "description": "Keyboard for automation engineers",
  "price": 85.90,
  "stock": 20
}
```

Example response:

```json
{
  "id": 3,
  "name": "Mechanical Keyboard",
  "description": "Keyboard for automation engineers",
  "price": 85.90,
  "stock": 20,
  "active": true
}
```

## Error Handling

The API uses a standardized error response:

```json
{
  "timestamp": "2026-07-06T16:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product with id 999 was not found",
  "path": "/api/products/999",
  "details": []
}
```

Validation errors return HTTP 400:

```json
{
  "timestamp": "2026-07-06T16:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/products",
  "details": [
    "name: Product name is required",
    "price: Product price must be greater than zero"
  ]
}
```

## How to Run Locally

From this folder:

```bash
backend/qa-store-api
```

Run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will run on:

```text
http://localhost:8080
```

## How to Run Tests

```bash
./mvnw clean test
```

On Windows:

```powershell
.\mvnw.cmd clean test
```

## Current Testing Scope

The backend currently includes:

- Spring context loading test
- Health controller test
- Product controller integration test
- Success scenarios
- Not found scenario
- Validation error scenario

## Engineering Decisions

### Why use an in-memory service first?

The Product module currently uses an in-memory implementation to validate the API contract before introducing persistence.

This allows the project to evolve incrementally:

```text
Spring Boot API
     |
Validation
     |
Error Handling
     |
Testing
     |
Database Integration
```

### Why use an interface for ProductService?

The controller depends on `ProductService`, not directly on `InMemoryProductService`.

This follows the Dependency Inversion Principle and will make it easier to replace the in-memory implementation with a database-backed service in the next phase.

## Next Phase

The next phase will introduce:

- Spring Data JPA
- MySQL
- Product entity
- Product repository
- Database-backed service implementation
- Database integration tests

## Swagger UI

This backend exposes Swagger UI using Springdoc OpenAPI.

Start the application:

```powershell
$env:DB_PASSWORD="your_mysql_password"
.\mvnw.cmd spring-boot:run


## Running with Docker

The backend can run inside Docker together with a MySQL container.

Docker files:

```text
backend/qa-store-api/Dockerfile
backend/qa-store-api/.dockerignore
docker/docker-compose.yml
docker/.env.example