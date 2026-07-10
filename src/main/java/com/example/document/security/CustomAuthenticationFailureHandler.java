package com.example.document.security;

import com.example.document.entity.User;
import com.example.document.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 5 * 60 * 1000;

    private final UserRepository userRepository;

    public CustomAuthenticationFailureHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        
        if (username != null) {
            incrementFailedAttempts(username);
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", exception.getMessage());
        
        response.sendRedirect("/login?error=true");
    }

    @Transactional
    private void incrementFailedAttempts(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int newAttempts = user.getFailedAttempts() != null ? user.getFailedAttempts() + 1 : 1;
            user.setFailedAttempts(newAttempts);
            
            if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLockedUntil(new Date(System.currentTimeMillis() + LOCK_TIME_DURATION));
            }
            
            userRepository.save(user);
        }
    }
}