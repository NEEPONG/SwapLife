package com.springboot.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.dto.UserDTO;
import com.springboot.model.User;
import com.springboot.repository.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${file.upload-dir}")
	private String uploadDir;

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
    
    public void updateUserProfile(String username, String firstName, String lastName, MultipartFile profileImage) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        if (profileImage != null && !profileImage.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir);

            // ✅ ตรวจสอบและสร้างโฟลเดอร์อัตโนมัติ
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("✅ [DEBUG] Created upload directory: " + uploadPath.toAbsolutePath());
            }

            String originalFilename = profileImage.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;
            Path filePath = uploadPath.resolve(fileName);

            // ✅ เขียนไฟล์ลงในโฟลเดอร์
            Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // ✅ ลบรูปเก่าถ้ามี
            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                try {
                    String oldPath = user.getProfilePictureUrl().replaceFirst("^/", "");
                    Files.deleteIfExists(Paths.get(oldPath));
                } catch (Exception e) {
                    System.err.println("⚠️ [DEBUG] Cannot delete old image: " + e.getMessage());
                }
            }

            // ✅ อัปเดต path ให้ตรงกับ static resource
            user.setProfilePictureUrl("/uploads/" + fileName);
        }

        userRepository.save(user);
    }
    
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบผู้ใช้"));

        // ✅ ตรวจสอบรหัสผ่านเดิม
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("รหัสผ่านเดิมไม่ถูกต้อง");
        }

        // ✅ ตรวจสอบรหัสผ่านใหม่ตรงกันไหม
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("รหัสผ่านใหม่ไม่ตรงกัน");
        }

        // ✅ ตรวจสอบความยาวขั้นต่ำ
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("รหัสผ่านใหม่ต้องมีอย่างน้อย 6 ตัวอักษร");
        }

        // ✅ เข้ารหัสใหม่แล้วบันทึก
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}