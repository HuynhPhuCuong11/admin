package com.example.trasua.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @GetMapping("/login")
    public String loginPage(Authentication auth) {
        // Nếu đã đăng nhập thì redirect về dashboard
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }
}
