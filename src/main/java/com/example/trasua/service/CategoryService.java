package com.example.trasua.service;

import com.example.trasua.model.Category;
import com.example.trasua.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepo;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public List<Category> findRoots() {
        return categoryRepo.findByParentIdIsNullOrderBySortOrderAsc();
    }

    public List<Category> findChildren(Long parentId) {
        return categoryRepo.findByParentIdOrderBySortOrderAsc(parentId);
    }

    public List<Category> findActive() {
        return categoryRepo.findByActiveTrue();
    }

    public Category findById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
    }

    @Transactional
    public Category save(Category category) {
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            category.setSlug(ProductService.generateSlug(category.getName()));
        }
        String baseSlug = category.getSlug();
        String finalSlug = baseSlug;
        int i = 1;
        String finalSlug1 = finalSlug;
        while (categoryRepo.existsBySlug(finalSlug) &&
               (category.getId() == null || !categoryRepo.findById(category.getId())
                       .map(c -> c.getSlug().equals(finalSlug1)).orElse(false))) {
            finalSlug = baseSlug + "-" + i++;
        }
        category.setSlug(finalSlug);
        return categoryRepo.save(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepo.deleteById(id);
    }

    public long countAll() {
        return categoryRepo.count();
    }
}
