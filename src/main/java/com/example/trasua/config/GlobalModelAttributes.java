package com.example.trasua.config;

import com.example.trasua.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expose common attributes to all Thymeleaf views.
 *
 * Many templates read ${session.username}. This advice ensures it is always set
 * for authenticated users (Spring Security username = email in this project).
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final UserRepository userRepository;

    public GlobalModelAttributes(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void exposeUsername(HttpSession session) {
        if (session.getAttribute("username") != null) return;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) return;

        String email = auth.getName();

        // Prefer full name if present, fall back to email.
        String displayName = userRepository.findByEmail(email)
                .map(u -> {
                    try {
                        var m = u.getClass().getMethod("getFullName");
                        Object v = m.invoke(u);
                        return v != null && !v.toString().isBlank() ? v.toString() : null;
                    } catch (Exception ignored) {
                        return null;
                    }
                })
                .orElse(null);

        session.setAttribute("username", (displayName != null && !displayName.isBlank()) ? displayName : email);
    }
}
