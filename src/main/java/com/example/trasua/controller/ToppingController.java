package com.example.trasua.controller;

import com.example.trasua.model.Topping;
import com.example.trasua.service.ToppingService;
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
@RequestMapping("/admin/toppings")
public class ToppingController {

    @Autowired private ToppingService toppingService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String status,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        var pageResult = toppingService.search(keyword, status,
                PageRequest.of(page, 15, Sort.by("id").ascending()));

        model.addAttribute("toppings",    pageResult);
        model.addAttribute("keyword",     keyword);
        model.addAttribute("status",      status);
        model.addAttribute("pageTitle",   "Quản lý Topping");
        model.addAttribute("activePage",  "toppings");
        return "admin/toppings/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("topping",    new Topping());
        model.addAttribute("pageTitle",  "Thêm Topping");
        model.addAttribute("activePage", "toppings");
        return "admin/toppings/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("topping",    toppingService.findById(id));
        model.addAttribute("pageTitle",  "Chỉnh sửa Topping");
        model.addAttribute("activePage", "toppings");
        return "admin/toppings/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Topping topping,
                       BindingResult bindingResult,
                       RedirectAttributes redirect,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle",  "Topping");
            model.addAttribute("activePage", "toppings");
            return "admin/toppings/form";
        }
        try {
            toppingService.save(topping);
            redirect.addFlashAttribute("success", "Lưu topping thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/toppings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            toppingService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa topping!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/toppings";
    }

    @PostMapping("/toggle-stock/{id}")
    public String toggleStock(@PathVariable Long id) {
        toppingService.toggleStock(id);
        return "redirect:/admin/toppings";
    }
}
