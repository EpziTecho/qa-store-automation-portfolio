# ============================================================
# File: Dockerfile
# Module: Backend Containerization
#
# Responsibility:
# Builds and runs the QA Store Spring Boot API inside a Docker container.
#
# Interaction:
# docker-compose.yml uses this Dockerfile to build the qa-store-api service.
# The container connects to the MySQL container through environment variables.
#
# Design Pattern:
# Multi-stage Docker build.
#
# Engineering Principles:
# - Separation of build and runtime stages.
# - Smaller runtime image.
# - Reproducible local execution.
# - Environment-based configuration.
# ============================================================

# ----------------------------
# Build stage
# ----------------------------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper and Maven configuration first to improve Docker layer caching.
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable inside Linux container.
RUN chmod +x mvnw

# Download dependencies before copying source code.
# This improves rebuild speed when only source files change.
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy source code.
COPY src src

# Build the Spring Boot jar.
# Tests are skipped here because integration tests require external services.
# Tests remain executed locally and later in CI/CD.
RUN ./mvnw -q -DskipTests package

# ----------------------------
# Runtime stage
# ----------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy only the generated jar from the build stage.
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]