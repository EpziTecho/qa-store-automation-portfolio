package com.qastore.api.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/*
 * ============================================================
 * File: AuthenticatedUser.java
 * Module: User Security
 *
 * Responsibility:
 * Represents the authenticated user used internally by Spring Security.
 *
 * Interaction:
 * UserDetailsServiceImpl converts UserEntity into AuthenticatedUser.
 * JwtAuthenticationFilter stores this object inside the SecurityContext.
 * AuthController can read it from the Authentication principal.
 *
 * Design Pattern:
 * Adapter Pattern.
 *
 * Engineering Principles:
 * - Separation of concerns: persistence entity is not exposed directly to Spring Security.
 * - Security integration: adapts domain user data to UserDetails contract.
 * - Encapsulation: exposes only the user data needed for authentication/authorization.
 * ============================================================
 */

public class AuthenticatedUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final Boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(
            Long id,
            String email,
            String passwordHash,
            Boolean active,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = active;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /*
     * For this portfolio project, we do not model account expiration yet.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     * For this portfolio project, we do not model account locking yet.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     * For this portfolio project, credentials do not expire yet.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
     * Active users can authenticate.
     * Inactive users are treated as disabled.
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(active);
    }
}