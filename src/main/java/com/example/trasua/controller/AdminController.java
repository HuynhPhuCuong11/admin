package com.example.trasua.controller;

import com.example.trasua.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private OrderService   orderService;
    @Autowired private ProductService productService;
    @Autowired private UserService    userService;
    @Autowired private ToppingService toppingService;
    @Autowired private ReviewService  reviewService;
    @Autowired private CouponService  couponService;

    // ===== LOGIN =====
    @GetMapping("/login")
    public String loginPage(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) return "redirect:/admin/dashboard";
        return "admin/login";
    }

    // ===== DASHBOARD =====
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model m) {
        m.addAttribute("totalOrders",      orderService.countAll());
        m.addAttribute("pendingOrders",    orderService.countByStatus("pending"));
        m.addAttribute("processingOrders", orderService.countByStatus("processing"));
        m.addAttribute("deliveredOrders",  orderService.countByStatus("delivered"));
        m.addAttribute("cancelledOrders",  orderService.countByStatus("cancelled"));
        m.addAttribute("totalRevenue",     orderService.totalRevenue());
        m.addAttribute("revenueToday",     orderService.revenueToday());
        m.addAttribute("ordersToday",      orderService.ordersToday());
        m.addAttribute("totalProducts",    productService.countAll());
        m.addAttribute("publishedProducts",productService.countByStatus("published"));
        m.addAttribute("totalUsers",       userService.countAll());
        m.addAttribute("totalCustomers",   userService.countCustomers());
        m.addAttribute("totalToppings",    toppingService.countAll());
        m.addAttribute("pendingReviews",   reviewService.countPending());
        m.addAttribute("totalCoupons",     couponService.countAll());

        // Doanh thu 7 ngày — trả về JSON-friendly map cho Chart.js
        List<Object[]> rows = orderService.revenueByDay(7);
        Map<String, Object> revenueChart = new LinkedHashMap<>();
        rows.forEach(r -> revenueChart.put(String.valueOf(r[0]), r[1]));
        m.addAttribute("revenueChart", revenueChart);

        // Đơn hàng theo trạng thái — cho doughnut chart
        List<Object[]> statusRows = orderService.countGroupByStatus();
        Map<String, Object> statusChart = new LinkedHashMap<>();
        statusRows.forEach(r -> statusChart.put(String.valueOf(r[0]), r[1]));
        m.addAttribute("statusChart", statusChart);

        m.addAttribute("pageTitle",  "Dashboard");
        m.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    // ===== STATS API (trả JSON cho chart reload) =====
    @GetMapping("/api/stats/revenue")
    @ResponseBody
    public Map<String, Object> revenueStats(@RequestParam(defaultValue = "7") int days) {
        Map<String, Object> map = new LinkedHashMap<>();
        orderService.revenueByDay(days).forEach(r -> map.put(String.valueOf(r[0]), r[1]));
        return map;
    }
}
