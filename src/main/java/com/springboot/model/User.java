package com.springboot.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users") // บอกให้ JPA รู้ว่าคลาสนี้เชื่อมกับตารางชื่อ "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // กำหนดให้ ID เป็น Auto-Increment
    private Integer userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String firstName;
    private String lastName;
    private String profilePictureUrl;

    @CreationTimestamp // Hibernate จะใส่เวลาที่สร้างให้โดยอัตโนมัติ
    private LocalDateTime createdAt;

    // ความสัมพันธ์: User หนึ่งคนสามารถมี Item ได้หลายชิ้น (One-to-Many)
    @OneToMany(mappedBy = "user")
    private List<Item> items;
}