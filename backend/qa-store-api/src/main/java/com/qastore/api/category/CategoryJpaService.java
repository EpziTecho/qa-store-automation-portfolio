package com.qastore.api.category;

import com.qastore.api.product.ProductRepository;
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
 * CategoryJpaService delegates category persistence to CategoryRepository.
 * It also consults ProductRepository before deleting a category to enforce
 * business integrity rules.
 *
 * Design Pattern:
 * Service Layer + Repository Pattern.
 *
 * Engineering Principles:
 * - Dependency Inversion: implements CategoryService abstraction.
 * - Separation of concerns: business operations are separated from HTTP handling.
 * - Transaction management: database operations are executed inside transactions.
 * - Business validation: duplicate category names are rejected.
 * - Referential integrity: categories with active products cannot be deactivated.
 * ============================================================
 */

@Service
@Transactional
public class CategoryJpaService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryJpaService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAllByActiveTrueOrderByIdAsc()
                .stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findByIdAndActiveTrue(id)
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

    @Override
    public Category update(Long id, UpdateCategoryRequest request) {
        CategoryEntity category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new DuplicateCategoryException(request.name());
        }

        category.updateDetails(
                request.name(),
                request.description());

        CategoryEntity updatedCategory = categoryRepository.save(category);

        return CategoryMapper.toDomain(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        CategoryEntity category = categoryRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        boolean hasActiveProducts = productRepository.existsByCategoryIdAndActiveTrue(id);

        if (hasActiveProducts) {
            throw new CategoryHasActiveProductsException(id);
        }

        category.deactivate();

        categoryRepository.save(category);
    }
}