package com.example.trasua.service;

import com.example.trasua.model.Coupon;
import com.example.trasua.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired private CouponRepository couponRepo;

    public Page<Coupon> search(String keyword, String status, Pageable pageable) {
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String st = (status == null || status.isBlank()) ? null : status;
        return couponRepo.search(kw, st, pageable);
    }

    public List<Coupon> findAll() {
        return couponRepo.findAll();
    }

    public Coupon findById(Long id) {
        return couponRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon không tồn tại"));
    }

    public Optional<Coupon> findByCode(String code) {
        return couponRepo.findByCode(code);
    }

    @Transactional
    public Coupon save(Coupon coupon) {
        coupon.setCode(coupon.getCode().trim().toUpperCase());
        return couponRepo.save(coupon);
    }

    @Transactional
    public void delete(Long id) {
        couponRepo.deleteById(id);
    }

    @Transactional
    public void toggleStatus(Long id) {
        Coupon c = findById(id);
        c.setStatus("active".equals(c.getStatus()) ? "inactive" : "active");
        couponRepo.save(c);
    }

    public long countAll() { return couponRepo.count(); }
    public long countActive() { return couponRepo.countByStatus("active"); }

    /**
     * Validate a coupon code against an order amount.
     * Returns the discount amount, or throws RuntimeException if invalid.
     */
    public BigDecimal validate(String code, BigDecimal orderAmount) {
        Coupon coupon = couponRepo.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Mã coupon không tồn tại"));

        if (!"active".equals(coupon.getStatus())) {
            throw new RuntimeException("Mã coupon không còn hiệu lực");
        }
        if (coupon.isExpired()) {
            throw new RuntimeException("Mã coupon đã hết hạn");
        }
        if (coupon.isUsageLimitReached()) {
            throw new RuntimeException("Mã coupon đã đạt giới hạn sử dụng");
        }
        if (coupon.getStartDate() != null && coupon.getStartDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Mã coupon chưa đến ngày hiệu lực");
        }
        if (coupon.getMinOrderAmount() != null
                && orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu để sử dụng coupon");
        }

        BigDecimal discount;
        if ("percent".equals(coupon.getDiscountType())) {
            discount = orderAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
            if (coupon.getMaxDiscountAmount() != null
                    && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        } else {
            discount = coupon.getDiscountValue();
        }

        return discount.min(orderAmount);
    }
}
