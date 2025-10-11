package com.springboot.repository;

import com.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data JPA จะสร้างเมธอดพื้นฐาน (save, findById, findAll, delete) ให้เอง
    // เราสามารถเพิ่มเมธอดค้นหาที่ซับซ้อนขึ้นได้เอง เช่น
    // User findByUsername(String username);
}