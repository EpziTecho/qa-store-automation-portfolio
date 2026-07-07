package com.qastore.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qastore.api.common.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/*
 * ============================================================
 * File: RestAccessDeniedHandler.java
 * Module: Security Configuration
 *
 * Responsibility:
 * Converts authorization failures into a consistent JSON error response.
 *
 * Interaction:
 * SecurityConfig registers this component as the AccessDeniedHandler.
 * Spring Security calls it when an authenticated user tries to access an
 * endpoint without the required authority.
 *
 * Design Pattern:
 * Access Denied Handler / Strategy.
 *
 * Engineering Principles:
 * - API consistency: authorization errors use the same ErrorResponse contract.
 * - Separation of concerns: security error formatting is centralized.
 * - RESTful behavior: authenticated users without permission receive HTTP 403.
 * ============================================================
 */

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "Access is denied",
                request.getRequestURI(),
                List.of());

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}