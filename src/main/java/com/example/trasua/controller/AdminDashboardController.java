package com.example.trasua.controller;

import com.example.trasua.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class AdminDashboardController {

    @Autowired private OrderService orderService;
    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    @Autowired private ToppingService toppingService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Stats cards
        model.addAttribute("totalOrders",     orderService.countAll());
        model.addAttribute("pendingOrders",   orderService.countByStatus("pending"));
        model.addAttribute("processingOrders",orderService.countByStatus("processing"));
        model.addAttribute("deliveredOrders", orderService.countByStatus("delivered"));
        model.addAttribute("cancelledOrders", orderService.countByStatus("cancelled"));

        model.addAttribute("totalRevenue",    orderService.totalRevenue());
        model.addAttribute("revenueToday",    orderService.revenueToday());
        model.addAttribute("ordersToday",     orderService.ordersToday());

        model.addAttribute("totalProducts",   productService.countAll());
        model.addAttribute("publishedProducts", productService.countByStatus("published"));

        model.addAttribute("totalUsers",      userService.countAll());
        model.addAttribute("totalCustomers",  userService.countCustomers());

        model.addAttribute("totalToppings",   toppingService.countAll());

        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }
}
