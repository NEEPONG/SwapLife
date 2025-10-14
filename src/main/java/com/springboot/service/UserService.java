package com.springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.dto.UserDTO;
import com.springboot.model.User;
import com.springboot.repository.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
    	if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("ชื่อผู้ใช้นี้ถูกใช้งานแล้ว");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("อีเมลล์นี้ถูกใช้งานแล้ว");
        }
        // เข้ารหัส password ก่อนบันทึก
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
    }
    
    public UserDTO getUserProfile(String username) {
        return userRepository.findUserDTOByUsername(username)
                .orElseThrow(() -> new RuntimeException("ไม่พบผู้ใช้"));
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}