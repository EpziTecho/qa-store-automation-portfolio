package com.qastore.api.auth;

import com.qastore.api.user.RoleEntity;
import com.qastore.api.user.UserEntity;
import com.qastore.api.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * ============================================================
 * File: AuthService.java
 * Module: Authentication
 *
 * Responsibility:
 * Contains authentication business logic.
 *
 * Interaction:
 * AuthController delegates login requests to this service.
 * AuthService uses UserRepository to find users, PasswordEncoder to verify
 * raw passwords against BCrypt hashes and JwtService to generate access tokens.
 *
 * Design Pattern:
 * Service Layer.
 *
 * Engineering Principles:
 * - Separation of concerns: authentication logic is outside the controller.
 * - Security: compares passwords using PasswordEncoder.matches().
 * - Encapsulation: UserEntity is not exposed directly to API clients.
 * - Stateless authentication: returns a JWT access token after successful login.
 * ============================================================
 */

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /*
     * Validates user credentials and generates a JWT access token.
     *
     * Security decisions:
     * - If the email does not exist, return a generic authentication error.
     * - If the password is incorrect, return the same generic error.
     * - If the user is inactive, return the same generic error.
     *
     * This prevents account enumeration through different error messages.
     */
    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash());

        if (!passwordMatches || !user.getActive()) {
            throw new InvalidCredentialsException();
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .map(Enum::name)
                .sorted()
                .toList();

        String accessToken = jwtService.generateToken(user);

        return new LoginResponse(
                true,
                user.getId(),
                user.getEmail(),
                roles,
                accessToken,
                "Bearer",
                jwtService.getExpirationMs(),
                "Login successful");
    }
}