# OpenAPI Documentation - QA Store API

## 1. Purpose

This document describes how OpenAPI and Swagger UI are used in the QA Store API.

The goal is to provide an interactive and machine-readable API contract that can be used by backend developers, QA engineers, frontend developers and DevOps pipelines.

---

## 2. Tool Used

The project uses Springdoc OpenAPI.

Springdoc automatically scans Spring Boot controllers, DTOs and validation annotations to generate an OpenAPI 3 specification.

Dependency used:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
</dependency>