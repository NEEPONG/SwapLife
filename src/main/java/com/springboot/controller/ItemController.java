package com.springboot.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.model.*;
import com.springboot.model.enums.ItemCondition;
import com.springboot.model.enums.ListingType;
import com.springboot.service.*;

@Controller
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/post")
	public String showPostItemPage(Model model) {
		model.addAttribute("item", new Item());
		model.addAttribute("categories", categoryService.getAllCategories());
		return "post";
	}
	
	@PostMapping("/add")
	public String addItem(
			@RequestParam("title") String title,
			@RequestParam("description") String description,
			@RequestParam("itemCondition") String itemConditionStr,
			@RequestParam("category.categoryId") Integer categoryId,
			@RequestParam("listingType") String listingTypeStr,
			@RequestParam(value = "desiredItems", required = false) String desiredItems,
			@RequestParam("thumbnail") MultipartFile thumbnail,
			@RequestParam("images") List<MultipartFile> images,
			Authentication authentication,
			RedirectAttributes redirectAttributes,
			Model model) {

		System.out.println("🔥 [DEBUG] ========== Received POST /items/add ==========");
		System.out.println("🔥 [DEBUG] Title: " + title);
		System.out.println("🔥 [DEBUG] Description: " + description);
		System.out.println("🔥 [DEBUG] Item condition: " + itemConditionStr);
		System.out.println("🔥 [DEBUG] Category ID: " + categoryId);
		System.out.println("🔥 [DEBUG] Listing type: " + listingTypeStr);
		System.out.println("🔥 [DEBUG] Desired items: " + desiredItems);
		System.out.println("🔥 [DEBUG] Thumbnail: " + (thumbnail != null ? thumbnail.getOriginalFilename() : "null"));
		System.out.println("🔥 [DEBUG] Images count: " + (images != null ? images.size() : 0));

		// ✅ Validation: ตรวจสอบ Title
		if (title == null || title.trim().isEmpty()) {
			System.out.println("❌ [ERROR] Title is empty");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณากรอกชื่อสินค้า");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Description
		if (description == null || description.trim().isEmpty()) {
			System.out.println("❌ [ERROR] Description is empty");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณากรอกคำอธิบายสินค้า");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Item Condition
		if (itemConditionStr == null || itemConditionStr.trim().isEmpty()) {
			System.out.println("❌ [ERROR] Item condition is empty");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกสภาพสิ่งของ");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Category
		if (categoryId == null) {
			System.out.println("❌ [ERROR] Category is null");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกหมวดหมู่");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Listing Type
		if (listingTypeStr == null || listingTypeStr.trim().isEmpty()) {
			System.out.println("❌ [ERROR] Listing type is empty");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกประเภทการลงประกาศ");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Thumbnail
		if (thumbnail == null || thumbnail.isEmpty()) {
			System.out.println("❌ [ERROR] Thumbnail is empty");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาอัปโหลดภาพหลัก");
			return "post";
		}

		// ✅ Validation: ตรวจสอบ Images
		if (images == null || images.isEmpty() || images.stream().allMatch(MultipartFile::isEmpty)) {
			System.out.println("❌ [ERROR] No images uploaded");
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาอัปโหลดภาพเสริมอย่างน้อย 1 รูป");
			return "post";
		}

		try {
			// ✅ ดึง User จาก Authentication
			String username = authentication.getName();
			User user = userService.findByUsername(username);
			
			if (user == null) {
				System.out.println("❌ [ERROR] User not found: " + username);
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ไม่พบข้อมูลผู้ใช้ กรุณาเข้าสู่ระบบใหม่");
				return "post";
			}
			System.out.println("✅ [DEBUG] User found: " + user.getUsername() + " (ID: " + user.getUserId() + ")");

			// ✅ สร้าง Item Object
			Item item = new Item();
			item.setTitle(title.trim());
			item.setDescription(description.trim());
			item.setDesiredItems(desiredItems != null ? desiredItems.trim() : null);
			item.setUser(user);
			
			// ✅ แปลง ItemCondition String เป็น Enum
			try {
				ItemCondition itemCondition = ItemCondition.valueOf(itemConditionStr);
				item.setItemCondition(itemCondition);
				System.out.println("✅ [DEBUG] Item condition set: " + itemCondition);
			} catch (IllegalArgumentException e) {
				System.out.println("❌ [ERROR] Invalid item condition: " + itemConditionStr);
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "สภาพสิ่งของไม่ถูกต้อง");
				return "post";
			}
			
			// ✅ แปลง ListingType String เป็น Enum
			try {
				ListingType listingType = ListingType.valueOf(listingTypeStr);
				item.setListingType(listingType);
				System.out.println("✅ [DEBUG] Listing type set: " + listingType);
			} catch (IllegalArgumentException e) {
				System.out.println("❌ [ERROR] Invalid listing type: " + listingTypeStr);
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ประเภทการลงประกาศไม่ถูกต้อง");
				return "post";
			}
			
			// ✅ ดึง Category จาก Database
			Category category = categoryService.getCategoryById(categoryId);
			if (category == null) {
				System.out.println("❌ [ERROR] Category not found with ID: " + categoryId);
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ไม่พบหมวดหมู่ที่เลือก");
				return "post";
			}
			item.setCategory(category);
			System.out.println("✅ [DEBUG] Category set: " + category.getName() + " (ID: " + category.getCategoryId() + ")");

			// ✅ บันทึก Item พร้อมรูปภาพ
			System.out.println("📸 [DEBUG] Starting to save item with images...");
			itemService.saveItemWithImages(item, thumbnail, images);
			System.out.println("✅ [SUCCESS] ========== Item saved successfully with ID: " + item.getItemId() + " ==========");

			// ✅ Redirect พร้อม Success Message
			redirectAttributes.addFlashAttribute("successMessage", "🎉 เพิ่มสิ่งของเรียบร้อยแล้ว!");
			return "redirect:/items/post";

		} catch (Exception e) {
			System.out.println("❌ [ERROR] ========== Exception occurred ==========");
			System.out.println("❌ [ERROR] Message: " + e.getMessage());
			e.printStackTrace();
			
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
			return "post";
		}
	}
}