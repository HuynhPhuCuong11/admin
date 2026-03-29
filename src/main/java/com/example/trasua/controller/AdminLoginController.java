package com.example.trasua.controller;

import org.springframework.security.core.Authentication;

public class AdminLoginController {

    public String loginPage(Authentication auth) {
        // Nếu đã đăng nhập thì redirect về dashboard
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/admin/dashboard";
        }
        return "admin/login";
    }
}
