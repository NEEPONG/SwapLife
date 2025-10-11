package com.springboot.service;

import com.springboot.model.User;
import com.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ฉีด (Inject) PasswordEncoder ที่เราสร้างไว้ใน Bean เข้ามาใช้งาน
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * เมธอดสำหรับลงทะเบียนผู้ใช้ใหม่
     * @param username ชื่อผู้ใช้
     * @param rawPassword รหัสผ่านแบบดิบ (ยังไม่เข้ารหัส)
     * @return User ที่บันทึกแล้ว
     */
    public User registerNewUser(String username, String rawPassword) {
        User newUser = new User();
        newUser.setUsername(username);
        
        // --- นี่คือส่วนที่ทันสมัย ---
        // 1. เข้ารหัสผ่านด้วย BCrypt (มันจะสร้าง Salt และ Hash ให้เอง)
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPasswordHash(hashedPassword);
        
        // 2. บันทึกลงฐานข้อมูล
        return userRepository.save(newUser);
    }

    /**
     * เมธอดสำหรับตรวจสอบรหัสผ่านตอน Login
     * @param rawPassword รหัสผ่านที่ผู้ใช้กรอก
     * @param hashedPassword รหัสผ่านที่เข้ารหัสแล้วจากฐานข้อมูล
     * @return true ถ้ารหัสผ่านถูกต้อง, false ถ้าไม่ถูกต้อง
     */
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        // --- และนี่คือส่วนที่ใช้ตรวจสอบ ---
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}