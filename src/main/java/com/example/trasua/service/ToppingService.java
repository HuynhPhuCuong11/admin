package com.example.trasua.service;

import com.example.trasua.model.Topping;
import com.example.trasua.repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ToppingService {

    @Autowired private ToppingRepository toppingRepo;

    public Page<Topping> search(String keyword, String status, Pageable pageable) {
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String st = (status == null || status.isBlank()) ? null : status;
        return toppingRepo.search(kw, st, pageable);
    }

    public List<Topping> findAll() {
        return toppingRepo.findAll();
    }

    public Topping findById(Long id) {
        return toppingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Topping không tồn tại"));
    }

    @Transactional
    public Topping save(Topping topping) {
        return toppingRepo.save(topping);
    }

    @Transactional
    public void delete(Long id) {
        toppingRepo.deleteById(id);
    }

    @Transactional
    public void toggleStock(Long id) {
        Topping t = findById(id);
        t.setInStock(!t.getInStock());
        toppingRepo.save(t);
    }

    public long countAll() {
        return toppingRepo.count();
    }
}
