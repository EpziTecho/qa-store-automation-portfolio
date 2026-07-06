package com.qastore.api.product;

import com.qastore.api.category.CategoryEntity;
import com.qastore.api.category.CategoryNotFoundException;
import com.qastore.api.category.CategoryRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * ============================================================
 * File: ProductJpaService.java
 * Module: Product Persistence Service
 *
 * Responsibility:
 * Provides a database-backed implementation of ProductService using MySQL.
 *
 * Interaction:
 * ProductController depends on ProductService.
 * Spring injects this implementation by default because it is registered as a service.
 * ProductJpaService delegates product persistence to ProductRepository and validates
 * category existence through CategoryRepository.
 *
 * Design Pattern:
 * Service Layer + Repository Pattern.
 *
 * Engineering Principles:
 * - Dependency Inversion: implements ProductService abstraction.
 * - Separation of concerns: business operations are separated from HTTP handling.
 * - Transaction management: database operations are executed inside transactions.
 * - Referential integrity: products must reference an existing category.
 * - Soft delete: delete operations deactivate records instead of removing them.
 * ============================================================
 */

@Service
@Profile("!in-memory")
@Transactional
public class ProductJpaService implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductJpaService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAllByActiveTrueOrderByIdAsc()
                .stream()
                .map(ProductMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .map(ProductMapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(CreateProductRequest request) {
        CategoryEntity category = findCategoryOrThrow(request.categoryId());

        ProductEntity entity = ProductMapper.toEntity(request, category);

        ProductEntity savedEntity = productRepository.save(entity);

        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    public Product update(Long id, UpdateProductRequest request) {
        ProductEntity product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        CategoryEntity category = findCategoryOrThrow(request.categoryId());

        product.updateDetails(
                request.name(),
                request.description(),
                request.price(),
                request.stock(),
                category);

        ProductEntity updatedProduct = productRepository.save(product);

        return ProductMapper.toDomain(updatedProduct);
    }

    @Override
    public void delete(Long id) {
        ProductEntity product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.deactivate();

        productRepository.save(product);
    }

    /*
     * Centralizes category lookup for create and update operations.
     *
     * This avoids duplicated repository calls and keeps the error behavior
     * consistent across product use cases.
     */
    private CategoryEntity findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}