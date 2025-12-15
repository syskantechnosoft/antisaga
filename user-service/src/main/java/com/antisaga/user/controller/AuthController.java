package com.antisaga.user.controller;

import com.antisaga.user.dto.LoginRequest;
import com.antisaga.user.entity.User;
import com.antisaga.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
             throw new RuntimeException("Username already exists");
        }
        // Also check email
        if(userRepository.existsByEmail(user.getEmail())) {
             throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(u -> u.getPassword().equals(loginRequest.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
