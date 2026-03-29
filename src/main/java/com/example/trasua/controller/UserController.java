package com.example.trasua.controller;

import com.example.trasua.model.User;
import com.example.trasua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired private UserService svc;

    @GetMapping
    public String list(@RequestParam(defaultValue="") String keyword,
                       @RequestParam(defaultValue="") String status,
                       @RequestParam(defaultValue="") String role,
                       @RequestParam(defaultValue="0") int page, Model m) {
        m.addAttribute("users", svc.search(keyword, status, role,
                PageRequest.of(page, 15, Sort.by("createdAt").descending())));
        m.addAttribute("keyword",keyword); m.addAttribute("status",status); m.addAttribute("role",role);
        m.addAttribute("pageTitle","Người dùng"); m.addAttribute("activePage","users");
        return "admin/users/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("user", svc.findById(id));
        m.addAttribute("pageTitle","Sửa người dùng"); m.addAttribute("activePage","users");
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User u,
                       @RequestParam(required=false) String rawPassword,
                       RedirectAttributes ra) {
        try { svc.save(u, rawPassword); ra.addFlashAttribute("success","Lưu người dùng thành công!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Lỗi: "+e.getMessage()); }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        svc.updateStatus(id, status);
        ra.addFlashAttribute("success","Cập nhật trạng thái thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try { svc.delete(id); ra.addFlashAttribute("success","Đã xóa!"); }
        catch (Exception e) { ra.addFlashAttribute("error","Không thể xóa: "+e.getMessage()); }
        return "redirect:/admin/users";
    }
}
