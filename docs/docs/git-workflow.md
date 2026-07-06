# Git Workflow

## Responsibility

This document defines the Git and GitHub workflow used in the QA Store Automation Portfolio.

The goal is to work as if the project were developed by a professional software engineering team.

## Main Branch

```text
main
```

The `main` branch represents the stable version of the project.

No direct development should be done on `main`.

## Feature Branches

Each module or feature must be developed in an isolated branch.

Examples:

```text
feature/project-architecture
feature/springboot-backend
feature/mysql-database
feature/swagger-openapi
feature/jwt-auth
feature/docker-environment
feature/postman-api-testing
feature/cypress-framework
feature/cypress-api-testing
feature/cypress-mocking
feature/selenium-testng
feature/rest-assured
feature/github-actions
feature/azure-devops
feature/professional-readme
feature/deploy
```

## Commit Convention

The project will use descriptive commits.

Examples:

```text
docs: define project architecture
feat: create product module
test: add product API tests
fix: resolve authentication error
ci: add backend pipeline
refactor: improve service layer
chore: update project configuration
```

## Commit Types

| Type | Usage |
|---|---|
| docs | Documentation changes |
| feat | New feature |
| fix | Bug fix |
| test | Test creation or update |
| ci | CI/CD pipeline changes |
| refactor | Internal improvement without behavior change |
| chore | Configuration or maintenance tasks |

## Professional Workflow

```text
1. Create feature branch
2. Implement changes
3. Run local validations
4. Commit changes
5. Push branch
6. Open Pull Request
7. Review changes
8. Merge into main
```

## Why This Workflow Matters

This workflow helps demonstrate:

- Professional Git usage
- Separation of work
- Traceable changes
- Controlled integration
- Clean repository history
- Interview-ready engineering discipline