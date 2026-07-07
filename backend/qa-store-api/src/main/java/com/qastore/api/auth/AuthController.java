package com.qastore.api.auth;

import com.qastore.api.common.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * ============================================================
 * File: AuthController.java
 * Module: Authentication
 *
 * Responsibility:
 * Exposes authentication endpoints.
 *
 * Interaction:
 * Receives login requests, delegates credential validation to AuthService
 * and returns authentication responses.
 *
 * Design Pattern:
 * MVC Controller pattern.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: handles only authentication HTTP operations.
 * - Separation of concerns: authentication logic is delegated to AuthService.
 * - RESTful API design: uses HTTP status codes to represent authentication outcomes.
 * ============================================================
 */

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Operations for user authentication")
public class AuthController {

    private final AuthService authService;

    /*
     * Constructor injection makes dependencies explicit and testable.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
     * POST /api/auth/login
     *
     * Validates user credentials.
     *
     * In the next block this endpoint will return a JWT access token.
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Validates user credentials against the stored BCrypt password hash. JWT generation will be added in the next phase block.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}