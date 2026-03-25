package com.example.trasua.config;

import com.example.trasua.model.User;
import com.example.trasua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tạo tài khoản admin mặc định nếu chưa có
        if (!userRepo.existsByEmail("admin@trasua.vn")) {
            User admin = new User();
            admin.setEmail("admin@trasua.vn");
            admin.setFullName("Quản trị viên");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRoles("admin");
            admin.setStatus("active");
            admin.setEmailVerified(true);
            userRepo.save(admin);
            System.out.println("✅ Tạo tài khoản admin mặc định: admin@trasua.vn / admin123");
        }
    }
}
