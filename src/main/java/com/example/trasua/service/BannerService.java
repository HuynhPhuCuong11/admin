package com.example.trasua.service;

import com.example.trasua.model.Banner;
import com.example.trasua.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BannerService {
    @Autowired
    private BannerRepository repo;
    public List<Banner> findAll()    { return repo.findAll(); }
    public Banner findById(Long id)  { return repo.findById(id).orElseThrow(()->new RuntimeException("Banner không tồn tại")); }
    @Transactional public Banner save(Banner b) { return repo.save(b); }
    @Transactional public void delete(Long id)  { repo.deleteById(id); }
    @Transactional public void toggle(Long id)  {
        Banner b = findById(id); b.setActive(!Boolean.TRUE.equals(b.getActive())); repo.save(b);
    }
}
