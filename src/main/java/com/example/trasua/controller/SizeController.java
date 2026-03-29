package com.example.trasua.controller;

import com.example.trasua.model.Size;
import com.example.trasua.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sizes")
public class SizeController {
    @Autowired private SizeService svc;

    @GetMapping
    public String list(Model m) {
        m.addAttribute("sizes", svc.findAll());
        m.addAttribute("newSize", new Size());
        m.addAttribute("pageTitle","Quản lý Size"); m.addAttribute("activePage","sizes");
        return "admin/sizes/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Size s, RedirectAttributes ra) {
        try { svc.save(s); ra.addFlashAttribute("success","Lưu size thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/sizes";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/sizes";
    }
}
