package com.qastore.api.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * ============================================================
 * File: UserDetailsServiceImpl.java
 * Module: User Security
 *
 * Responsibility:
 * Loads application users from MySQL and adapts them to Spring Security's
 * UserDetails contract.
 *
 * Interaction:
 * JwtAuthenticationFilter uses this service after extracting the email from
 * a valid JWT. Spring Security uses the returned UserDetails object as the
 * authenticated principal.
 *
 * Design Pattern:
 * Adapter + Service Layer.
 *
 * Engineering Principles:
 * - Dependency Inversion: Spring Security depends on UserDetailsService abstraction.
 * - Separation of concerns: user loading is isolated from JWT parsing logic.
 * - Security: inactive or missing users are rejected.
 * ============================================================
 */

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Loads a user by email.
     *
     * In this project, email is the username used by Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UsernameNotFoundException("User was not found");
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getActive(),
                authorities);
    }
}