package com.qastore.api.common;

import com.qastore.api.category.CategoryNotFoundException;
import com.qastore.api.category.DuplicateCategoryException;
import com.qastore.api.product.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.qastore.api.category.CategoryHasActiveProductsException;
import java.time.LocalDateTime;
import java.util.List;
import com.qastore.api.auth.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
/*
 * ============================================================
 * File: GlobalExceptionHandler.java
 * Module: Common Error Handling
 *
 * Responsibility:
 * Centralizes exception handling for REST controllers.
 *
 * Interaction:
 * Catches exceptions thrown by controllers/services and converts them into
 * consistent HTTP error responses.
 *
 * Design Pattern:
 * Controller Advice / Global Exception Handler.
 *
 * Engineering Principles:
 * - Separation of concerns: controllers do not manually build error responses.
 * - DRY: error handling is centralized.
 * - Consistent API contracts.
 * - Fail fast with meaningful client feedback.
 * ============================================================
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleProductNotFound(
                        ProductNotFoundException exception,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                List.of());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationErrors(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                List<String> details = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                                .toList();

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Request validation failed",
                                request.getRequestURI(),
                                details);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }

        @ExceptionHandler(CategoryNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCategoryNotFound(
                        CategoryNotFoundException exception,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                List.of());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(DuplicateCategoryException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateCategory(
                        DuplicateCategoryException exception,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                List.of());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(CategoryHasActiveProductsException.class)
        public ResponseEntity<ErrorResponse> handleCategoryHasActiveProducts(
                        CategoryHasActiveProductsException exception,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                List.of());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleInvalidCredentials(
                        InvalidCredentialsException exception,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                exception.getMessage(),
                                request.getRequestURI(),
                                List.of());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
}