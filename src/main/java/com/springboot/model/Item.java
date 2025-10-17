package com.springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.springboot.model.enums.ItemCondition;
import com.springboot.model.enums.ItemStatus;
import com.springboot.model.enums.ListingType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @NotBlank(message = "กรุณากรอกชื่อสินค้า")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "กรุณากรอกคำอธิบายสินค้า")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @NotNull(message = "กรุณาระบุสภาพสิ่งของ")
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition;

    @NotNull(message = "กรุณาระบุประเภทการลงประกาศ")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListingType listingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status = ItemStatus.ว่าง;

    private String desiredItems;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt = null;

    // ความสัมพันธ์
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @NotNull(message = "กรุณาเข้าสู่ระบบก่อนเพิ่มสินค้า")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    @NotNull(message = "กรุณาเลือกหมวดหมู่")
    private Category category;

    // ✅ ไม่ต้องมี @NotNull เพราะเราจะสร้าง ItemImage ใน Service
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> images = new ArrayList<>();

    // convenience getter for template: return thumbnail image URL or first image or placeholder
    public String getThumbnailUrl() {
        if (images != null && !images.isEmpty()) {
            for (ItemImage img : images) {
                if (img.isThumbnail()) return img.getImageUrl();
            }
            return images.get(0).getImageUrl();
        }
        return "/images/placeholder.png";
    }
}