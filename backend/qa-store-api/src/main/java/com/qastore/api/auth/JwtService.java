package com.qastore.api.auth;

import com.qastore.api.user.RoleEntity;
import com.qastore.api.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/*
 * ============================================================
 * File: JwtService.java
 * Module: Authentication
 *
 * Responsibility:
 * Generates and validates JSON Web Tokens used by the QA Store API.
 *
 * Interaction:
 * AuthService uses this service after successful credential validation.
 * Future JwtAuthenticationFilter will use this service to validate incoming
 * Authorization Bearer tokens.
 *
 * Design Pattern:
 * Service Layer.
 *
 * Engineering Principles:
 * - Single Responsibility Principle: JWT creation and validation are centralized.
 * - Security by configuration: signing secret is externalized through environment variables.
 * - Stateless authentication: generated tokens contain enough identity data for API requests.
 * - Maintainability: token logic is isolated from controllers and business services.
 * ============================================================
 */

@Service
public class JwtService {

    private final String jwtSecret;
    private final long jwtExpirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String jwtSecret,
            @Value("${app.jwt.expiration-ms}") long jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /*
     * Validates JWT configuration during application startup.
     *
     * This prevents the application from starting with an empty, invalid or weak
     * JWT secret.
     */
    @PostConstruct
    void validateConfiguration() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured using JWT_SECRET environment variable");
        }

        if (jwtExpirationMs <= 0) {
            throw new IllegalStateException("JWT expiration time must be greater than zero");
        }

        /*
         * Forces decoding and key validation at startup.
         *
         * Keys.hmacShaKeyFor will fail if the decoded secret is not strong enough
         * for HMAC signing.
         */
        signingKey();
    }

    /*
     * Generates a JWT for an authenticated user.
     *
     * Claims included:
     * - subject: user email
     * - userId: database user ID
     * - roles: assigned application roles
     */
    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(jwtExpirationMs);

        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .map(Enum::name)
                .sorted()
                .toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(signingKey())
                .compact();
    }

    /*
     * Extracts all claims from a token after validating its signature.
     *
     * If the token is invalid, expired or modified, JJWT will throw an exception.
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /*
     * Extracts the username used by Spring Security.
     *
     * In this project, the email is the authentication username.
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /*
     * Returns the configured expiration time.
     *
     * AuthService uses this value to inform clients how long the access token is
     * valid.
     */
    public long getExpirationMs() {
        return jwtExpirationMs;
    }

    /*
     * Builds the HMAC signing key from a Base64-encoded secret.
     *
     * The secret is not stored directly in code. It must come from JWT_SECRET.
     */
    private SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}