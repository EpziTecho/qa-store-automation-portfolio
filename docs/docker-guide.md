# Docker Guide - QA Store Automation Portfolio

## 1. Purpose

This document explains how Docker is used in the QA Store Automation Portfolio project.

Docker is used to provide a reproducible local environment for the Spring Boot backend and the MySQL database.

The goal is to allow developers, QA engineers and CI/CD pipelines to run the application with a consistent infrastructure setup.

---

## 2. Docker Architecture

The local Docker environment contains two main services:

```text
qa-store-api
qa-store-mysql