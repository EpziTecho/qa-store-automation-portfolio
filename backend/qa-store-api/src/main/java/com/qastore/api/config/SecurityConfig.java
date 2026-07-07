package com.qastore.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * ============================================================
 * File: SecurityConfig.java
 * Module: Security Configuration
 *
 * Responsibility:
 * Defines the initial Spring Security configuration for the QA Store API.
 *
 * Interaction:
 * Spring Security reads this configuration to decide which HTTP requests are
 * allowed, which authentication mechanisms are enabled and how sessions are handled.
 *
 * Design Pattern:
 * Configuration Class.
 *
 * Engineering Principles:
 * - Separation of concerns: security rules are centralized.
 * - Explicit configuration: avoids Spring Security default behavior surprising the API.
 * - Stateless API design: prepares the application for JWT authentication.
 * - Progressive delivery: keeps current endpoints public until JWT login is implemented.
 * ============================================================
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     * Defines the HTTP security filter chain.
     *
     * In this first JWT phase, endpoints remain public to avoid breaking the
     * current Product, Category, Health and Swagger flows.
     *
     * Later, when JWT login is implemented, we will change this configuration:
     * - /api/auth/** will remain public.
     * - GET endpoints may remain public depending on the business decision.
     * - POST, PUT and DELETE endpoints will require authentication.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * CSRF is disabled because this backend is a stateless REST API.
                 *
                 * CSRF protection is mainly relevant for browser-based sessions
                 * using cookies. JWT APIs usually authenticate each request with
                 * an Authorization header instead.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * Stateless session management means Spring Security will not
                 * create or use HTTP sessions to remember authentication state.
                 *
                 * This is required for JWT because each request must carry its own
                 * authentication token.
                 */
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /*
                 * Authorization rules.
                 *
                 * For now, we explicitly permit the endpoints already implemented.
                 * This prevents the default Spring Security login page or 401
                 * responses from breaking the existing application and tests.
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**")
                        .permitAll()

                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()

                        /*
                         * Any unknown route is denied by default.
                         *
                         * This is safer than permitAll for everything because it
                         * forces us to explicitly document and expose new routes.
                         */
                        .anyRequest().denyAll())

                /*
                 * Disable form login because this is not a server-rendered web app.
                 */
                .formLogin(AbstractHttpConfigurer::disable)

                /*
                 * Disable HTTP Basic because authentication will be based on JWT.
                 */
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /*
     * PasswordEncoder used to hash user passwords.
     *
     * BCrypt is a strong adaptive hashing algorithm commonly used for password
     * storage. We never store plain text passwords in the database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}