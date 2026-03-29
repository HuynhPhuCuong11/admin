package com.example.trasua.controller;

import com.example.trasua.model.Banner;
import com.example.trasua.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/banners")
public class BannerController {
    @Autowired private BannerService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("banners",   svc.findAll());
        m.addAttribute("pageTitle","Banner"); m.addAttribute("activePage","banners");
        return "admin/banners/list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        m.addAttribute("banner", new Banner());
        m.addAttribute("pageTitle","Thêm banner"); m.addAttribute("activePage","banners");
        return "admin/banners/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("banner", svc.findById(id));
        m.addAttribute("pageTitle","Sửa banner"); m.addAttribute("activePage","banners");
        return "admin/banners/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Banner b, RedirectAttributes ra) {
        try { svc.save(b); ra.addFlashAttribute("success","Lưu banner thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/banners";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/banners";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) { svc.toggle(id); return "redirect:/admin/banners"; }
}
