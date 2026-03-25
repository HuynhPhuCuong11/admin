package com.example.trasua.controller;

import com.example.trasua.model.Banner;
import com.example.trasua.service.BannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/banners")
public class BannerController {

    @Autowired private BannerService bannerService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("banners",    bannerService.findAll());
        model.addAttribute("pageTitle",  "Quản lý Banner");
        model.addAttribute("activePage", "banners");
        return "admin/banners/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("banner",     new Banner());
        model.addAttribute("pageTitle",  "Thêm Banner");
        model.addAttribute("activePage", "banners");
        return "admin/banners/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("banner",     bannerService.findById(id));
        model.addAttribute("pageTitle",  "Chỉnh sửa Banner");
        model.addAttribute("activePage", "banners");
        return "admin/banners/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Banner banner,
                       BindingResult bindingResult,
                       RedirectAttributes redirect,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle",  "Banner");
            model.addAttribute("activePage", "banners");
            return "admin/banners/form";
        }
        try {
            bannerService.save(banner);
            redirect.addFlashAttribute("success", "Lưu banner thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/banners";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            bannerService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa banner!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/banners";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        bannerService.toggleStatus(id);
        return "redirect:/admin/banners";
    }
}
