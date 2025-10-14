package com.springboot.repository;

import com.springboot.dto.UserDTO;
import com.springboot.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	// ✅ ตรวจสอบว่ามี username นี้อยู่ในระบบหรือไม่
    boolean existsByUsername(String username);

    // ✅ ตรวจสอบว่ามี email นี้อยู่ในระบบหรือไม่
    boolean existsByEmail(String email);
    
 // ✅ ดึงเฉพาะ field ที่ต้องใช้ แล้ว map เป็น DTO
    @Query("SELECT new com.springboot.dto.UserDTO(u.username, u.email, u.firstName, u.lastName, u.passwordHash) "
         + "FROM User u WHERE u.username = :username")
    Optional<UserDTO> findUserDTOByUsername(String username);
    
    Optional<User> findByUsername(String username);
}