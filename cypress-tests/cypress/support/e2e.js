/*
 * ============================================================
 * File: e2e.js
 * Module: Cypress Support Setup
 *
 * Responsibility:
 * Loads global Cypress support code before test specs are executed.
 *
 * Interaction:
 * Cypress loads this file automatically because it is configured as supportFile
 * in cypress.config.js.
 *
 * Design Pattern:
 * Test Support Bootstrap.
 *
 * Engineering Principles:
 * - Centralized test setup.
 * - Reusable custom command registration.
 * - Keeps spec files focused on test intent.
 * ============================================================
 */

import "./commands";
import "./interceptCommands";
