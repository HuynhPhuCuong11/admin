package com.example.trasua.service;

import com.example.trasua.model.Banner;
import com.example.trasua.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BannerService {

    @Autowired private BannerRepository bannerRepo;

    public List<Banner> findAll() {
        return bannerRepo.findAll();
    }

    public List<Banner> findActive() {
        return bannerRepo.findByStatusOrderBySortOrderAsc("active");
    }

    public Banner findById(Long id) {
        return bannerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner không tồn tại"));
    }

    @Transactional
    public Banner save(Banner banner) {
        return bannerRepo.save(banner);
    }

    @Transactional
    public void delete(Long id) {
        bannerRepo.deleteById(id);
    }

    @Transactional
    public void toggleStatus(Long id) {
        Banner b = findById(id);
        b.setStatus("active".equals(b.getStatus()) ? "inactive" : "active");
        bannerRepo.save(b);
    }

    public long countAll() { return bannerRepo.count(); }
}
