package com.qastore.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * ============================================================
 * File: QaStoreApiApplication.java
 * Module: Application Bootstrap
 *
 * Responsibility:
 * Main entry point of the QA Store Spring Boot application.
 *
 * Interaction:
 * Starts the Spring application context, embedded server and component scanning.
 * It automatically detects controllers, services and configuration classes
 * under the com.qastore.api package.
 *
 * Design Pattern:
 * Application Bootstrap pattern.
 *
 * Engineering Principles:
 * - Convention over Configuration: Spring Boot automatically configures the application.
 * - Separation of Concerns: this class only starts the application.
 * ============================================================
 */

/**
 * Main class of the QA Store API.
 *
 * The @SpringBootApplication annotation enables:
 * - Component scanning.
 * - Auto-configuration.
 * - Spring Boot application configuration.
 */
@SpringBootApplication
public class QaStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(QaStoreApiApplication.class, args);
	}
}