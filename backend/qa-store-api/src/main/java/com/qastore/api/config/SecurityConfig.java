package com.qastore.api.config;

import com.qastore.api.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * ============================================================
 * File: SecurityConfig.java
 * Module: Security Configuration
 *
 * Responsibility:
 * Defines Spring Security configuration for the QA Store API.
 *
 * Interaction:
 * Registers JwtAuthenticationFilter in the Spring Security filter chain.
 * Defines which endpoints are public and which endpoints require authentication.
 * Configures stateless session management for JWT-based authentication.
 *
 * Design Pattern:
 * Configuration Class.
 *
 * Engineering Principles:
 * - Separation of concerns: security rules are centralized.
 * - Explicit configuration: avoids accidental exposure of endpoints.
 * - Stateless API design: authentication is based on JWT instead of HTTP sessions.
 * - Progressive delivery: only /api/auth/me is protected in this block.
 * ============================================================
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    /*
     * Defines the HTTP security filter chain.
     *
     * JwtAuthenticationFilter runs before UsernamePasswordAuthenticationFilter
     * so that Bearer tokens can authenticate requests before authorization rules
     * are evaluated.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health").permitAll()

                        /*
                         * Login must remain public because users need it to obtain JWTs.
                         */
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        /*
                         * This endpoint is protected and proves that JWT validation works.
                         */
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**")
                        .permitAll()

                        /*
                         * Product and Category remain public until the next block,
                         * where we will enforce role-based authorization.
                         */
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()

                        .anyRequest().denyAll())

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                /*
                 * Adds the JWT filter into the security chain.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * PasswordEncoder used to hash user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}