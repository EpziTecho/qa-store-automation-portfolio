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
 * Defines authentication and authorization rules for the QA Store API.
 *
 * Interaction:
 * Registers JwtAuthenticationFilter in the Spring Security filter chain.
 * Defines public endpoints, authenticated endpoints and admin-only endpoints.
 * Configures stateless session management for JWT-based authentication.
 *
 * Design Pattern:
 * Configuration Class.
 *
 * Engineering Principles:
 * - Separation of concerns: security rules are centralized.
 * - Explicit authorization: each route group declares its access policy.
 * - Stateless API design: authentication is based on JWT instead of HTTP sessions.
 * - Principle of least privilege: write operations require ROLE_ADMIN.
 * ============================================================
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint,
            RestAccessDeniedHandler restAccessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    /*
     * Defines the HTTP security filter chain.
     *
     * JwtAuthenticationFilter runs before UsernamePasswordAuthenticationFilter
     * so Bearer tokens can authenticate requests before authorization rules
     * are evaluated.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler))

                .authorizeHttpRequests(auth -> auth
                        /*
                         * Operational endpoint.
                         */
                        .requestMatchers("/api/health").permitAll()

                        /*
                         * Authentication endpoints.
                         */
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()

                        /*
                         * Swagger/OpenAPI endpoints remain public so the API contract
                         * can be explored during development.
                         */
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**")
                        .permitAll()

                        /*
                         * Product catalog read operations are public.
                         */
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        /*
                         * Product catalog write operations require administrator authority.
                         */
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("ROLE_ADMIN")

                        /*
                         * Category read operations are public.
                         */
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        /*
                         * Category write operations require administrator authority.
                         */
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("ROLE_ADMIN")

                        /*
                         * Any route not explicitly configured is denied.
                         */
                        .anyRequest().denyAll())

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

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