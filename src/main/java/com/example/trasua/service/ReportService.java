package com.example.trasua.service;

import com.example.trasua.repository.OrderRepository;
import com.example.trasua.repository.ProductRepository;
import com.example.trasua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ReportService {

    @Autowired private OrderRepository orderRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;

    // ---- Revenue ----
    public BigDecimal totalRevenue() {
        return orderRepo.sumRevenue();
    }

    public BigDecimal revenueToday() {
        return orderRepo.sumRevenueFrom(todayStart());
    }

    public BigDecimal revenueThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return orderRepo.sumRevenueFrom(monthStart);
    }

    // ---- Orders ----
    public long totalOrders() { return orderRepo.count(); }
    public long ordersToday() { return orderRepo.countOrdersFrom(todayStart()); }
    public long pendingOrders() { return orderRepo.countByOrderStatus("pending"); }
    public long processingOrders() { return orderRepo.countByOrderStatus("processing"); }
    public long deliveredOrders() { return orderRepo.countByOrderStatus("delivered"); }
    public long cancelledOrders() { return orderRepo.countByOrderStatus("cancelled"); }

    // ---- Products ----
    public long totalProducts() { return productRepo.count(); }
    public long publishedProducts() { return productRepo.countByStatus("published"); }

    // ---- Users ----
    public long totalUsers() { return userRepo.count(); }
    public long totalCustomers() { return userRepo.countByRoles("customer"); }

    private LocalDateTime todayStart() {
        return LocalDateTime.now().toLocalDate().atStartOfDay();
    }
}
