package com.springboot.runner; // สร้าง package ใหม่เพื่อความเป็นระเบียบ

import com.springboot.model.Category;
import com.springboot.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // 1. บอกให้ Spring รู้ว่านี่คือ Component ที่ต้องจัดการ
public class DataInitializer implements CommandLineRunner { // 2. Implement CommandLineRunner

    private final CategoryRepository categoryRepository;

    // 3. ฉีด (Inject) Repository เข้ามาใช้งานผ่าน Constructor
    @Autowired
    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 4. โค้ดในนี้จะทำงานอัตโนมัติ 1 ครั้ง ตอนแอปฯ เปิด
//        System.out.println("--- [SwapLife] เริ่มต้นสร้างข้อมูลหมวดหมู่ (Categories) ---");

//        // สร้าง Object Category ที่ 1: เสื้อผ้า
//        Category cat1 = new Category();
//        cat1.setName("เสื้อผ้า");
//        cat1.setDescription("เสื้อผ้าแฟชั่น, ชุดทำงาน, และเครื่องแต่งกายต่างๆ");
//        cat1.setImageUrl("/images/categories/clothing.jpg");
//
//        // สร้าง Object Category ที่ 2: เครื่องใช้ไฟฟ้า
//        Category cat2 = new Category();
//        cat2.setName("เครื่องใช้ไฟฟ้า");
//        cat2.setDescription("อุปกรณ์อิเล็กทรอนิกส์และเครื่องใช้ไฟฟ้าภายในบ้าน");
//        cat2.setImageUrl("/images/categories/electronics.jpg");
//
//        // สร้าง Object Category ที่ 3: หนังสือ
//        Category cat3 = new Category();
//        cat3.setName("หนังสือ");
//        cat3.setDescription("หนังสือ, นิตยสาร, และการ์ตูน");
//        cat3.setImageUrl("/images/categories/books.jpg");
//
//        // 5. บันทึก Object ทั้งหมดลงฐานข้อมูล
//        categoryRepository.save(cat1);
//        categoryRepository.save(cat2);
//        categoryRepository.save(cat3);

//        System.out.println("--- [SwapLife] บันทึกข้อมูลหมวดหมู่ 3 รายการเรียบร้อยแล้ว ---");
    }
}