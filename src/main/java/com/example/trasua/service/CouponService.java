package com.example.trasua.service;

import com.example.trasua.model.Coupon;
import com.example.trasua.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CouponService {
    @Autowired private CouponRepository repo;

    public Page<Coupon> search(String kw, Boolean active, Pageable p) {
        String k = (kw == null || kw.isBlank()) ? null : kw.trim();
        return repo.search(k, active, p);
    }

    public Coupon findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Coupon không tồn tại"));
    }

    @Transactional
    public Coupon save(Coupon c) {
        c.setCode(c.getCode().toUpperCase().trim());
        //thêm ràng buôc về giá trị giảm và giá trị tăng
        if ("percent".equals(c.getType()) && c.getValue() != null) {
            if (c.getValue().compareTo(BigDecimal.ZERO) < 0)
                throw new RuntimeException("Giá trị giảm không được âm");
            if (c.getValue().compareTo(new BigDecimal("100")) > 0)
                throw new RuntimeException("Giá trị giảm theo % không được vượt quá 100%");
        }

        if (c.getId() == null && repo.existsByCode(c.getCode()))
            throw new RuntimeException("Mã coupon đã tồn tại");
        if (c.getId() != null && repo.existsByCodeAndIdNot(c.getCode(), c.getId()))
            throw new RuntimeException("Mã coupon đã tồn tại");
        return repo.save(c);
    }

    @Transactional public void delete(Long id) { repo.deleteById(id); }

    @Transactional public void toggle(Long id) {
        Coupon c = findById(id); c.setActive(!Boolean.TRUE.equals(c.getActive())); repo.save(c);
    }

    public long countAll() { return repo.count(); }
}
