package com.example.trasua.repository;

import com.example.trasua.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIdIsNullOrderBySortOrderAsc();
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
    List<Category> findByActiveTrue();
    boolean existsBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories();
}
