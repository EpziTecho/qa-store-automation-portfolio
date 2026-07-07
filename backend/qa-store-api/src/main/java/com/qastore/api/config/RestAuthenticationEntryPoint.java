package com.qastore.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qastore.api.common.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/*
 * ============================================================
 * File: RestAuthenticationEntryPoint.java
 * Module: Security Configuration
 *
 * Responsibility:
 * Converts unauthenticated access attempts into a consistent JSON error response.
 *
 * Interaction:
 * SecurityConfig registers this component as the AuthenticationEntryPoint.
 * Spring Security calls it when a protected endpoint is accessed without valid
 * authentication.
 *
 * Design Pattern:
 * Entry Point / Strategy.
 *
 * Engineering Principles:
 * - API consistency: security errors use the same ErrorResponse contract.
 * - Separation of concerns: authentication error formatting is centralized.
 * - RESTful behavior: unauthenticated requests receive HTTP 401.
 * ============================================================
 */

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication is required",
                request.getRequestURI(),
                List.of());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}