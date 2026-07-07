package com.qastore.api.auth;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * ============================================================
 * File: JwtAuthenticationFilter.java
 * Module: Authentication
 *
 * Responsibility:
 * Validates incoming JWT Bearer tokens and registers authenticated users in
 * Spring Security's SecurityContext.
 *
 * Interaction:
 * SecurityConfig adds this filter before UsernamePasswordAuthenticationFilter.
 * JwtService validates token signature, subject and expiration.
 * UserDetailsService loads the authenticated user from MySQL.
 *
 * Design Pattern:
 * Security Filter.
 *
 * Engineering Principles:
 * - Stateless authentication: each request carries its own authentication token.
 * - Separation of concerns: token validation is isolated from controllers.
 * - Fail-safe behavior: invalid tokens clear the SecurityContext.
 * ============================================================
 */

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /*
     * Executes once per request.
     *
     * If there is no Authorization Bearer header, the request continues without
     * authentication. Public endpoints can still be accessed.
     *
     * If a valid token exists, the authenticated user is stored in the
     * SecurityContext for the rest of the request.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            String username = jwtService.extractUsername(token);

            boolean contextHasNoAuthentication = SecurityContextHolder.getContext().getAuthentication() == null;

            if (username != null && contextHasNoAuthentication) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException exception) {
            /*
             * Invalid, expired or malformed tokens must not authenticate the request.
             *
             * We do not write the response here. Authorization is handled later by
             * Spring Security. If the endpoint requires authentication, the configured
             * AuthenticationEntryPoint will return 401.
             */
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}