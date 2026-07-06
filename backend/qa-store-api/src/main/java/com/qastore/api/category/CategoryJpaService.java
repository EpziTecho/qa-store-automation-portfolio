package com.qastore.api.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * ============================================================
 * File: CategoryJpaService.java
 * Module: Category Persistence Service
 *
 * Responsibility:
 * Provides a database-backed implementation of CategoryService using MySQL.
 *
 * Interaction:
 * CategoryController depends on CategoryService.
 * CategoryJpaService delegates persistence operations to CategoryRepository.
 *
 * Design Pattern:
 * Service Layer + Repository Pattern.
 *
 * Engineering Principles:
 * - Dependency Inversion: implements CategoryService abstraction.
 * - Separation of concerns: business operations are separated from HTTP handling.
 * - Transaction management: database operations are executed inside transactions.
 * - Business validation: duplicate category names are rejected before persistence.
 * ============================================================
 */

@Service
@Transactional
public class CategoryJpaService implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryJpaService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAllByOrderByIdAsc()
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDomain)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category create(CreateCategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateCategoryException(request.name());
        }

        CategoryEntity entity = CategoryMapper.toEntity(request);
        CategoryEntity savedEntity = categoryRepository.save(entity);

        return CategoryMapper.toDomain(savedEntity);
    }
}