package com.example.trasua.controller;

import com.example.trasua.model.User;
import com.example.trasua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @RequestParam(defaultValue = "") String status,
                       @RequestParam(defaultValue = "") String role,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        var pageResult = userService.search(keyword, status, role,
                PageRequest.of(page, 15, Sort.by("createdAt").descending()));

        model.addAttribute("users",       pageResult);
        model.addAttribute("keyword",     keyword);
        model.addAttribute("status",      status);
        model.addAttribute("role",        role);
        model.addAttribute("pageTitle",   "Quản lý người dùng");
        model.addAttribute("activePage",  "users");
        return "admin/users/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user",       userService.findById(id));
        model.addAttribute("pageTitle",  "Chỉnh sửa người dùng");
        model.addAttribute("activePage", "users");
        return "admin/users/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user,
                       @RequestParam(required = false) String rawPassword,
                       RedirectAttributes redirect) {
        try {
            userService.save(user, rawPassword);
            redirect.addFlashAttribute("success", "Lưu người dùng thành công!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes redirect) {
        userService.updateStatus(id, status);
        redirect.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            userService.delete(id);
            redirect.addFlashAttribute("success", "Đã xóa người dùng!");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
