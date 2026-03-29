package com.example.trasua.repository;

import com.example.trasua.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIdIsNullOrderBySortOrderAsc();
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
    List<Category> findByActiveTrueOrderBySortOrderAsc();
    boolean existsBySlug(String slug);
}
