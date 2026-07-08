# Postman Guide - QA Store Automation Portfolio

## 1. Purpose

This document explains how Postman is used in the QA Store Automation Portfolio project.

Postman is used to validate the QA Store API manually and semi-automatically before implementing deeper automated API testing with REST Assured and CI/CD.

The goal is to provide a professional API testing workflow that covers:

- Health checks.
- Authentication.
- JWT token reuse.
- Public endpoints.
- Protected endpoints.
- CRUD workflows.
- Positive scenarios.
- Negative scenarios.
- Business validation.
- API regression validation.

---

## 2. Postman Structure

Postman files are located in:

```text
postman/
├── collections/
│   └── qa-store-api.postman_collection.json
└── environments/
    └── qa-store-local.postman_environment.json