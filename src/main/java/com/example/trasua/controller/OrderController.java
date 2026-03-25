package com.example.trasua.controller;

import com.example.trasua.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String orderStatus,
                       @RequestParam(defaultValue = "") String payStatus,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        var pageResult = orderService.search(keyword, orderStatus, payStatus,
                PageRequest.of(page, 15, Sort.by("createdAt").descending()));

        model.addAttribute("orders",       pageResult);
        model.addAttribute("keyword",      keyword);
        model.addAttribute("orderStatus",  orderStatus);
        model.addAttribute("payStatus",    payStatus);
        model.addAttribute("pageTitle",    "Quản lý đơn hàng");
        model.addAttribute("activePage",   "orders");
        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("order",       orderService.findById(id));
        model.addAttribute("pageTitle",   "Chi tiết đơn hàng");
        model.addAttribute("activePage",  "orders");
        return "admin/orders/detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam(required = false) String orderStatus,
                               @RequestParam(required = false) String payStatus,
                               @RequestParam(required = false) String note,
                               RedirectAttributes redirect) {
        try {
            orderService.updateStatus(id, orderStatus, payStatus, note);
            redirect.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @RequestParam(required = false) String reason,
                         RedirectAttributes redirect) {
        try {
            orderService.cancel(id, reason);
            redirect.addFlashAttribute("success", "Đã hủy đơn hàng!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }
}
