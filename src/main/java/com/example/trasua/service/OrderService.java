package com.example.trasua.service;

import com.example.trasua.model.Order;
import com.example.trasua.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired private OrderRepository repo;

    public Page<Order> search(String kw, String orderStatus, String payStatus, Pageable p) {
        return repo.search(nullIfBlank(kw), nullIfBlank(orderStatus), nullIfBlank(payStatus), p);
    }

    public Order findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
    }

    @Transactional
    public void updateStatus(Long id, String orderStatus, String payStatus, String note, String tracking) {
        Order o = findById(id);
        if (orderStatus != null && !orderStatus.isBlank()) o.setOrderStatus(orderStatus);
        if (payStatus   != null && !payStatus.isBlank())   o.setPayStatus(payStatus);
        if (note        != null && !note.isBlank())         o.setInternalNote(note);
        if (tracking    != null && !tracking.isBlank())     o.setTrackingNumber(tracking);
        repo.save(o);
    }

    @Transactional
    public void cancel(Long id, String reason) {
        Order o = findById(id);
        o.setOrderStatus("cancelled");
        o.setCancelReason(reason);
        repo.save(o);
    }

    // Stats
    public long countAll()              { return repo.count(); }
    public long countByStatus(String s) { return repo.countByOrderStatus(s); }
    public BigDecimal totalRevenue()    { return repo.sumRevenue(); }
    public BigDecimal revenueToday()    { return repo.sumRevenueFrom(LocalDateTime.now().toLocalDate().atStartOfDay()); }
    public long ordersToday()           { return repo.countFrom(LocalDateTime.now().toLocalDate().atStartOfDay()); }
    public List<Object[]> revenueByDay(int days) {
        return repo.revenueByDay(LocalDateTime.now().minusDays(days));
    }
    public List<Object[]> countGroupByStatus() { return repo.countGroupByStatus(); }

    private String nullIfBlank(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }}
