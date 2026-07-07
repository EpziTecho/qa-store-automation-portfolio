package com.qastore.api.auth;

import com.qastore.api.common.ErrorResponse;
import com.qastore.api.user.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

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
 * and exposes the current authenticated user from Spring Security.
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
     * Validates user credentials and returns a JWT access token.
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Validates user credentials against the stored BCrypt password hash and returns a JWT access token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    /*
     * GET /api/auth/me
     *
     * Returns the currently authenticated user.
     *
     * This endpoint is intentionally protected and is used to verify that JWT
     * authentication works end-to-end.
     */
    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user", description = "Returns the authenticated user extracted from the JWT Bearer token.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated user returned successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CurrentUserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication is required", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        List<String> roles = authenticatedUser.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .sorted()
                .toList();

        CurrentUserResponse response = new CurrentUserResponse(
                authenticatedUser.getId(),
                authenticatedUser.getEmail(),
                roles);

        return ResponseEntity.ok(response);
    }
}