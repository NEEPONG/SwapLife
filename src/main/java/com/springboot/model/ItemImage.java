package com.springboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item_images")
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(nullable = false)
    private String imageUrl;

    private boolean isThumbnail;

    // รูปภาพหลายรูป อยู่ใน Item ชิ้นเดียว (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
