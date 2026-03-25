package com.example.trasua.controller;

import com.example.trasua.model.Category;
import com.example.trasua.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories",  categoryService.findAll());
        model.addAttribute("pageTitle",   "Quản lý danh mục");
        model.addAttribute("activePage",  "categories");
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("category",   new Category());
        model.addAttribute("roots",      categoryService.findRoots());
        model.addAttribute("pageTitle",  "Thêm danh mục");
        model.addAttribute("activePage", "categories");
        return "admin/categories/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("category",   categoryService.findById(id));
        model.addAttribute("roots",      categoryService.findRoots());
        model.addAttribute("pageTitle",  "Chỉnh sửa danh mục");
        model.addAttribute("activePage", "categories");
        return "admin/categories/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category,
                       BindingResult bindingResult,
                       RedirectAttributes redirect,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roots",      categoryService.findRoots());
            model.addAttribute("pageTitle",  "Danh mục");
            model.addAttribute("activePage", "categories");
            return "admin/categories/form";
        }
        try {
            categoryService.save(category);
            redirect.addFlashAttribute("success", "Lưu danh mục thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            categoryService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa danh mục!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        Category c = categoryService.findById(id);
        c.setActive(!Boolean.TRUE.equals(c.getActive()));
        categoryService.save(c);
        return "redirect:/admin/categories";
    }
}
