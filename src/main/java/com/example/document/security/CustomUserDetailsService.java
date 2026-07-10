package com.example.document.security;

import com.example.document.entity.User;
import com.example.document.repository.UserRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名或密码错误"));
        
        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().after(new Date())) {
            long remainingSeconds = (user.getAccountLockedUntil().getTime() - System.currentTimeMillis()) / 1000;
            throw new AuthenticationException("账户已被锁定，请 " + remainingSeconds + " 秒后再试") {};
        }
        
        return user;
    }
}