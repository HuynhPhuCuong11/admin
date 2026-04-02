package com.example.trasua;

import com.example.trasua.model.User;
import com.example.trasua.repository.UserRepository;
import com.example.trasua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableWebSecurity
public class TrasuaAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrasuaAdminApplication.class, args);
    }

    // ===== PASSWORD ENCODER =====
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===== AUTH PROVIDER =====
    @Bean
    public DaoAuthenticationProvider authProvider(UserService userService,
                                                   PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userService);
        p.setPasswordEncoder(passwordEncoder);
        return p;
    }

    // ===== SECURITY FILTER CHAIN =====
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            DaoAuthenticationProvider authProvider) throws Exception {
        http
            .authenticationProvider(authProvider)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "STAFF")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/dashboard", true)
                .failureUrl("/admin/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
                .logout(logout -> logout
                        .logoutRequestMatcher(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/admin/logout"))
                        .logoutSuccessUrl("/admin/login?logout=true")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/api/**"));

        return http.build();
    }

    // ===== DATA INITIALIZER: tạo admin mặc định =====
    @Component
    static class DataInitializer implements CommandLineRunner {
        @Autowired UserRepository userRepo;
        @Autowired PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
            if (!userRepo.existsByEmail("admin@trasua.vn")) {
                User admin = new User();
                admin.setEmail("admin@trasua.vn");
                admin.setFullName("Quản trị viên");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRoles("admin");
                admin.setStatus("active");
                admin.setEmailVerified(true);
                userRepo.save(admin);
                System.out.println("✅ Admin mặc định: admin@trasua.vn / admin123");
            }
        }
    }
}
