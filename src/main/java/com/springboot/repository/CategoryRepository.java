package com.springboot.repository;

import com.springboot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // ✅ ดึงหมวดหมู่ 10 อันดับแรก (จัดเรียงตาม id หรือจะใช้ criteria อื่นได้)
    @Query("SELECT c FROM Category c ORDER BY c.categoryId ASC LIMIT 8")
    List<Category> findTop10Categories();
}
