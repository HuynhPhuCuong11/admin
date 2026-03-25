package com.example.trasua.service;

import com.example.trasua.model.User;
import com.example.trasua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepo;
    @Autowired @Lazy private PasswordEncoder passwordEncoder;

    // Dùng cho Spring Security login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRoles().toUpperCase())
                .build();
    }

    public Page<User> search(String keyword, String status, String role, Pageable pageable) {
        String kw = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String st = (status == null || status.isBlank()) ? null : status;
        String r  = (role == null || role.isBlank()) ? null : role;
        return userRepo.search(kw, st, r, pageable);
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
    }

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Transactional
    public User save(User user, String rawPassword) {
        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        return userRepo.save(user);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        User u = findById(id);
        u.setStatus(status);
        userRepo.save(u);
    }

    @Transactional
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    // ---- Stats ----
    public long countAll() { return userRepo.count(); }
    public long countCustomers() { return userRepo.countByRoles("customer"); }
    public long countActive() { return userRepo.countByStatus("active"); }
}
