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
    @Autowired private ToppingRepository repo;

    public Page<Topping> search(String kw, String status, Pageable p) {
        return repo.search(nullIfBlank(kw), nullIfBlank(status), p);
    }

    private String nullIfBlank(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }
    public List<Topping> findAll()    { return repo.findAll(); }
    public Topping findById(Long id)  { return repo.findById(id).orElseThrow(()->new RuntimeException("Topping không tồn tại")); }
    @Transactional public Topping save(Topping t) { return repo.save(t); }
    @Transactional public void delete(Long id)    { repo.deleteById(id); }
    @Transactional public void toggleStock(Long id) {
        Topping t = findById(id); t.setInStock(!Boolean.TRUE.equals(t.getInStock())); repo.save(t);
    }
    public long countAll() { return repo.count(); }

}
