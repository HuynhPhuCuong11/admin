package com.example.trasua.service;

import com.example.trasua.model.User;
import com.example.trasua.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy: " + email));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles(u.getRoles().toUpperCase())
                .build();
    }

    public Page<User> search(String kw, String status, String role, Pageable p) {
        return userRepo.search(blank(kw), blank(status), blank(role), p);
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

    @Transactional
    public User save(User user, String rawPassword) {
        if (rawPassword != null && !rawPassword.isBlank())
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepo.save(user);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        User u = findById(id);
        u.setStatus(status);
        userRepo.save(u);
    }

    @Transactional
    public void delete(Long id) { userRepo.deleteById(id); }

    public long countAll()       { return userRepo.count(); }
    public long countCustomers() { return userRepo.countByRoles("customer"); }
    public long countActive()    { return userRepo.countByStatus("active"); }

    private String blank(String s) { return (s == null || s.isBlank()) ? null : s.trim(); }
}
