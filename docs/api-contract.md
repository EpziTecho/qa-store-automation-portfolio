# API Contract

## Responsibility

This document describes the current REST API contract of the QA Store backend.

It defines endpoints, request bodies, response bodies, HTTP status codes and error formats.

## Base URL

Local environment:

```text
http://localhost:8080
```

## Health Endpoint

### GET /api/health

Validates that the API is running.

#### Response 200

```json
{
  "status": "UP",
  "service": "qa-store-api",
  "version": "1.0.0",
  "message": "QA Store API is running"
}
```

## Product Endpoints

## GET /api/products

Returns all products.

### Response 200

```json
[
  {
    "id": 1,
    "name": "Laptop QA Pro",
    "description": "Laptop designed for automation testing practice",
    "price": 1200.00,
    "stock": 10,
    "active": true
  },
  {
    "id": 2,
    "name": "Wireless Mouse",
    "description": "Wireless mouse for QA workstation",
    "price": 25.50,
    "stock": 50,
    "active": true
  }
]
```

---

## GET /api/products/{id}

Returns a product by id.

### Path Parameters

| Parameter | Type | Required | Description |
|---|---|---|---|
| `id` | number | yes | Product identifier |

### Response 200

```json
{
  "id": 1,
  "name": "Laptop QA Pro",
  "description": "Laptop designed for automation testing practice",
  "price": 1200.00,
  "stock": 10,
  "active": true
}
```

### Response 404

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

---

## POST /api/products

Creates a product.

### Request Body

```json
{
  "name": "Mechanical Keyboard",
  "description": "Keyboard for automation engineers",
  "price": 85.90,
  "stock": 20
}
```

### Field Validations

| Field | Validation |
|---|---|
| `name` | Required, not blank |
| `description` | Required, not blank |
| `price` | Required, greater than zero |
| `stock` | Required, zero or greater |

### Response 201

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

### Response Headers

```http
Location: /api/products/3
```

### Response 400

```json
{
  "timestamp": "2026-07-06T16:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/products",
  "details": [
    "name: Product name is required",
    "description: Product description is required",
    "price: Product price must be greater than zero",
    "stock: Product stock cannot be negative"
  ]
}
```

## Standard Error Response

All controlled API errors follow this structure:

```json
{
  "timestamp": "date-time",
  "status": 400,
  "error": "Bad Request",
  "message": "Error message",
  "path": "/api/example",
  "details": []
}
```

## Current API Status

| Endpoint | Status |
|---|---|
| `GET /api/health` | Implemented |
| `GET /api/products` | Implemented |
| `GET /api/products/{id}` | Implemented |
| `POST /api/products` | Implemented |
| `PUT /api/products/{id}` | Pending |
| `DELETE /api/products/{id}` | Pending |

## Next Contract Evolution

The API contract will evolve in future phases with:

- MySQL persistence
- Product update endpoint
- Product delete endpoint
- Category endpoints
- Authentication endpoints
- JWT-protected routes
- Swagger / OpenAPI documentation