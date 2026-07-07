package com.qastore.api.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/*
 * ============================================================
 * File: UserEntity.java
 * Module: User Security Persistence
 *
 * Responsibility:
 * Represents an application user persisted in MySQL.
 *
 * Interaction:
 * UserRepository manages UserEntity persistence.
 * UserEntity is related to RoleEntity through the user_roles join table.
 * Future authentication services will use this entity to validate credentials.
 *
 * Design Pattern:
 * JPA Entity.
 *
 * Engineering Principles:
 * - Security by design: stores password hashes, never plain text passwords.
 * - Separation of concerns: persistence model is isolated from login DTOs.
 * - Database integrity: email is unique and required.
 * - Authorization readiness: roles are modeled as a many-to-many relationship.
 * ============================================================
 */

@Entity
@Table(name = "app_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 120)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 120)
    private String lastName;

    /*
     * Email is used as the login identifier.
     */
    @Column(nullable = false, unique = true, length = 180)
    private String email;

    /*
     * Stores the BCrypt hash, not the original password.
     */
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /*
     * Many users can have many roles.
     *
     * FetchType.EAGER is acceptable here for the current security model because
     * roles will be needed immediately during authentication and authorization.
     *
     * In larger systems, LAZY loading with dedicated queries is often preferred.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    protected UserEntity() {
    }

    public UserEntity(
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            Boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = active;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.active == null) {
            this.active = true;
        }
    }

    /*
     * Adds a role to the user.
     *
     * The Set collection avoids duplicated role assignments.
     */
    public void addRole(RoleEntity role) {
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }
}