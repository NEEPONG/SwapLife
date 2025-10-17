package com.springboot.repository;

import com.springboot.model.Item;
import com.springboot.model.User;
import com.springboot.model.enums.ItemStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    // Spring Data JPA จะสร้างเมธอดพื้นฐาน (save, findById, findAll, delete) ให้เอง
    // เราสามารถเพิ่มเมธอดค้นหาที่ซับซ้อนขึ้นได้เอง เช่น
    // User findByUsername(String username);
	
	@Query("""
	        SELECT i FROM Item i
	        JOIN i.category c
	        WHERE 
	            (:keyword = '' OR LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) 
	             OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
	             OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
	        AND (:type = 'all' OR i.listingType = 
	                CASE 
	                    WHEN :type = 'swap' THEN 'แลกเปลี่ยน'
	                    WHEN :type = 'donate' THEN 'บริจาค'
	                    ELSE i.listingType
	                END)
	        AND (:category = 'all' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%')))
	        AND (:condition = 'all' OR i.itemCondition = 
	                CASE 
	                    WHEN :condition = 'new' THEN 'ใหม่'
	                    WHEN :condition = 'good' THEN 'เหมือนใหม่'
	                    WHEN :condition = 'fair' THEN 'ใช้งานแล้ว'
	                    ELSE i.itemCondition
	                END)
	        ORDER BY i.createdAt DESC
	        """)
	    List<Item> searchAndFilter(@Param("keyword") String keyword,
	                               @Param("type") String type,
	                               @Param("category") String category,
	                               @Param("condition") String condition);
	
	List<Item> findByUserOrderByCreatedAtDesc(User user);
	
	List<Item> findByUserAndStatusOrderByCreatedAtDesc(User user, ItemStatus status);

}
