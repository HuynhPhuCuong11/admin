package com.example.trasua.controller;

import com.example.trasua.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired private ReportService reportService;

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("totalRevenue",      reportService.totalRevenue());
        model.addAttribute("revenueToday",      reportService.revenueToday());
        model.addAttribute("revenueThisMonth",  reportService.revenueThisMonth());

        model.addAttribute("totalOrders",       reportService.totalOrders());
        model.addAttribute("ordersToday",       reportService.ordersToday());
        model.addAttribute("pendingOrders",     reportService.pendingOrders());
        model.addAttribute("processingOrders",  reportService.processingOrders());
        model.addAttribute("deliveredOrders",   reportService.deliveredOrders());
        model.addAttribute("cancelledOrders",   reportService.cancelledOrders());

        model.addAttribute("totalProducts",     reportService.totalProducts());
        model.addAttribute("publishedProducts", reportService.publishedProducts());

        model.addAttribute("totalUsers",        reportService.totalUsers());
        model.addAttribute("totalCustomers",    reportService.totalCustomers());

        model.addAttribute("pageTitle",  "Báo cáo & Thống kê");
        model.addAttribute("activePage", "reports");
        return "admin/reports/index";
    }
}
