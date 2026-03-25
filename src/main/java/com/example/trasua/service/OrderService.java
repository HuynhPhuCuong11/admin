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

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepo;

    public Page<Order> search(String keyword, String orderStatus, String payStatus, Pageable pageable) {
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String os = (orderStatus == null || orderStatus.isBlank()) ? null : orderStatus;
        String ps = (payStatus == null || payStatus.isBlank()) ? null : payStatus;
        return orderRepo.search(kw, os, ps, pageable);
    }

    public Order findById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
    }

    @Transactional
    public void updateStatus(Long id, String orderStatus, String payStatus, String note) {
        Order o = findById(id);
        if (orderStatus != null && !orderStatus.isBlank()) o.setOrderStatus(orderStatus);
        if (payStatus != null && !payStatus.isBlank()) o.setPayStatus(payStatus);
        if (note != null && !note.isBlank()) o.setInternalNote(note);
        orderRepo.save(o);
    }

    @Transactional
    public void cancel(Long id, String reason) {
        Order o = findById(id);
        o.setOrderStatus("cancelled");
        o.setCancelReason(reason);
        orderRepo.save(o);
    }

    // ---- Dashboard stats ----
    public long countAll() { return orderRepo.count(); }
    public long countByStatus(String status) { return orderRepo.countByOrderStatus(status); }
    public BigDecimal totalRevenue() { return orderRepo.sumRevenue(); }

    public BigDecimal revenueToday() {
        return orderRepo.sumRevenueFrom(LocalDateTime.now().toLocalDate().atStartOfDay());
    }

    public long ordersToday() {
        return orderRepo.countOrdersFrom(LocalDateTime.now().toLocalDate().atStartOfDay());
    }
}
