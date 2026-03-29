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
    @Autowired private CategoryService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("categories", svc.findAll());
        m.addAttribute("pageTitle","Danh mục"); m.addAttribute("activePage","categories");
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("category", new Category()); m.addAttribute("roots", svc.findRoots());
        m.addAttribute("pageTitle","Thêm danh mục"); m.addAttribute("activePage","categories");
        return "admin/categories/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("category", svc.findById(id)); m.addAttribute("roots", svc.findRoots());
        m.addAttribute("pageTitle","Sửa danh mục"); m.addAttribute("activePage","categories");
        return "admin/categories/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category c, BindingResult br,
                       RedirectAttributes ra, Model m) {
        if (br.hasErrors()) {
            m.addAttribute("roots", svc.findRoots());
            m.addAttribute("pageTitle","Danh mục"); m.addAttribute("activePage","categories");
            return "admin/categories/form";
        }
        try { svc.save(c); ra.addFlashAttribute("success","Lưu danh mục thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/categories";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) { svc.toggle(id); return "redirect:/admin/categories"; }
}
