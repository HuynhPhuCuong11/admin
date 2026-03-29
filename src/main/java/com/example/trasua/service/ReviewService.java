package com.example.trasua.service;

import com.example.trasua.model.Review;
import com.example.trasua.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    @Autowired private ReviewRepository repo;

    public Page<Review> search(String kw, String status, Integer rating, Pageable p) {
        String k = (kw == null || kw.isBlank()) ? null : kw.trim();
        String s = (status == null || status.isBlank()) ? null : status;
        return repo.search(k, s, rating, p);
    }

    public Review findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Review không tồn tại"));
    }

    @Transactional public void approve(Long id) { Review r = findById(id); r.setStatus("approved"); r.setIsHidden(false); repo.save(r); }
    @Transactional public void reject(Long id)  { Review r = findById(id); r.setStatus("rejected"); r.setIsHidden(true);  repo.save(r); }
    @Transactional public void delete(Long id)  { repo.deleteById(id); }

    @Transactional
    public void reply(Long id, String replyText) {
        Review r = findById(id); r.setAdminReply(replyText); repo.save(r);
    }

    public long countPending() { return repo.countByStatus("pending"); }
    public long countAll()     { return repo.count(); }
}
