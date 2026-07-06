package com.qastore.api.category;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 * ============================================================
 * File: CategoryEntity.java
 * Module: Category Persistence
 *
 * Responsibility:
 * Represents the database table used to persist product categories.
 *
 * Interaction:
 * CategoryRepository manages this entity using Spring Data JPA.
 * CategoryJpaService converts this entity into the internal Category domain model.
 *
 * Design Pattern:
 * JPA Entity.
 *
 * Engineering Principles:
 * - Separation of concerns: persistence model is separated from API DTOs.
 * - Single Responsibility Principle: this class represents database state only.
 * - ORM mapping: maps Java fields to relational database columns.
 * ============================================================
 */

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Category name.
     *
     * unique = true prevents duplicated category names at database level.
     */
    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected CategoryEntity() {
    }

    public CategoryEntity(String name, String description, Boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.active == null) {
            this.active = true;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /*
     * Updates mutable category fields.
     *
     * This method keeps update behavior inside the entity instead of exposing
     * public setters for every field.
     */
    public void updateDetails(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /*
     * Performs a logical deletion.
     *
     * The category remains in the database, but normal API queries will no longer
     * return it because repositories filter by active = true.
     */
    public void deactivate() {
        this.active = false;
    }
}