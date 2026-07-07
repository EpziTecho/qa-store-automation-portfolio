package com.qastore.api.user;

import jakarta.persistence.*;

/*
 * ============================================================
 * File: RoleEntity.java
 * Module: User Security Persistence
 *
 * Responsibility:
 * Represents a security role persisted in the database.
 *
 * Interaction:
 * UserEntity references RoleEntity through a many-to-many relationship.
 * RoleRepository manages role persistence.
 * Future JWT authorization will use roles as granted authorities.
 *
 * Design Pattern:
 * JPA Entity.
 *
 * Engineering Principles:
 * - Separation of concerns: role persistence is represented independently.
 * - Type safety: role names are stored using the RoleName enum.
 * - Database integrity: role names are unique.
 * ============================================================
 */

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Role name.
     *
     * EnumType.STRING stores values like ROLE_ADMIN instead of ordinal numbers.
     * This is safer because enum ordinal values can change when the enum evolves.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private RoleName name;

    @Column(nullable = false, length = 250)
    private String description;

    protected RoleEntity() {
    }

    public RoleEntity(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public RoleName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}