package com.springboot.model;

import com.springboot.model.enums.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING) // เก็บ Enum เป็น String ในฐานข้อมูล
    @Column(nullable = false)
    private ItemCondition itemCondition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingType listingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.ว่าง; // กำหนดค่าเริ่มต้น

    @Column(columnDefinition = "TEXT")
    private String desiredItems;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // --- ความสัมพันธ์ (Relationships) ---

    // Item หลายชิ้น อยู่ใน User คนเดียว (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // ระบุ Foreign Key
    private User user;

    // Item หลายชิ้น อยู่ใน Category เดียว (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Item หนึ่งชิ้น มีได้หลายรูป (One-to-Many)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> images;
}
