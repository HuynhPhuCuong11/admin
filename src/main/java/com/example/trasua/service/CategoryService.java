package com.example.trasua.service;

import com.example.trasua.model.Category;
import com.example.trasua.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repo;

    public List<Category> findAll()   { return repo.findAll(); }
    public List<Category> findRoots() { return repo.findByParentIdIsNullOrderBySortOrderAsc(); }
    public List<Category> findActive(){ return repo.findByActiveTrueOrderBySortOrderAsc(); }

    public Category findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
    }

    @Transactional
    public Category save(Category c) {
        if (c.getSlug() == null || c.getSlug().isBlank()) c.setSlug(ProductService.toSlug(c.getName()));
        String base = c.getSlug(), slug = base; int i = 1;
        while (repo.existsBySlug(slug)) {
            if (c.getId() != null) {
                final String check = slug; // capture current slug for lambda (must be effectively final)
                if (repo.findById(c.getId()).map(x -> x.getSlug().equals(check)).orElse(false)) break;
            }
            slug = base + "-" + i++;
        }
        c.setSlug(slug);
        return repo.save(c);
    }

    @Transactional
    public void delete(Long id)  { repo.deleteById(id); }

    @Transactional
    public void toggle(Long id)  { Category c = findById(id); c.setActive(!Boolean.TRUE.equals(c.getActive())); repo.save(c); }

    public long countAll() { return repo.count(); }
}
