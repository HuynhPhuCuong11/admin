package com.example.trasua.controller;

import com.example.trasua.model.Coupon;
import com.example.trasua.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

    @Autowired private CouponService couponService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        var pageResult = couponService.search(keyword, status,
                PageRequest.of(page, 15, Sort.by("createdAt").descending()));
        model.addAttribute("coupons",    pageResult);
        model.addAttribute("keyword",    keyword);
        model.addAttribute("status",     status);
        model.addAttribute("pageTitle",  "Quản lý Coupon");
        model.addAttribute("activePage", "coupons");
        return "admin/coupons/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("coupon",     new Coupon());
        model.addAttribute("pageTitle",  "Thêm Coupon");
        model.addAttribute("activePage", "coupons");
        return "admin/coupons/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("coupon",     couponService.findById(id));
        model.addAttribute("pageTitle",  "Chỉnh sửa Coupon");
        model.addAttribute("activePage", "coupons");
        return "admin/coupons/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Coupon coupon,
                       BindingResult bindingResult,
                       RedirectAttributes redirect,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle",  "Coupon");
            model.addAttribute("activePage", "coupons");
            return "admin/coupons/form";
        }
        try {
            couponService.save(coupon);
            redirect.addFlashAttribute("success", "Lưu coupon thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            couponService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa coupon!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        couponService.toggleStatus(id);
        return "redirect:/admin/coupons";
    }
}
