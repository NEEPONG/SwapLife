package com.springboot.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
    
    // ความสัมพันธ์: Category หนึ่งหมวดหมู่ สามารถมี Item ได้หลายชิ้น
    @OneToMany(mappedBy = "category")
    private List<Item> items;
}
