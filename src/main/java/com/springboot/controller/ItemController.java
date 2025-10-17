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
			@RequestParam("categoryId") Integer categoryId,
			@RequestParam("listingType") String listingTypeStr,
			@RequestParam(value = "desiredItems", required = false) String desiredItems,
			@RequestParam("thumbnail") MultipartFile thumbnail,
			@RequestParam("images") List<MultipartFile> images,
			Authentication authentication,
			RedirectAttributes redirectAttributes,
			Model model) {

		// Validation
		if (title == null || title.trim().isEmpty()) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณากรอกชื่อสินค้า");
			return "post";
		}

		if (description == null || description.trim().isEmpty()) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณากรอกคำอธิบายสินค้า");
			return "post";
		}

		if (itemConditionStr == null || itemConditionStr.trim().isEmpty()) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกสภาพสิ่งของ");
			return "post";
		}

		if (categoryId == null) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกหมวดหมู่");
			return "post";
		}

		if (listingTypeStr == null || listingTypeStr.trim().isEmpty()) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาเลือกประเภทการลงประกาศ");
			return "post";
		}

		if (thumbnail == null || thumbnail.isEmpty()) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาอัปโหลดภาพหลัก");
			return "post";
		}

		if (images == null || images.isEmpty() || images.stream().allMatch(MultipartFile::isEmpty)) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "กรุณาอัปโหลดภาพเสริมอย่างน้อย 1 รูป");
			return "post";
		}

		try {
			// ดึง User
			String username = authentication.getName();
			User user = userService.findByUsername(username);
			
			if (user == null) {
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ไม่พบข้อมูลผู้ใช้ กรุณาเข้าสู่ระบบใหม่");
				return "post";
			}

			// สร้าง Item
			Item item = new Item();
			item.setTitle(title.trim());
			item.setDescription(description.trim());
			item.setDesiredItems(desiredItems != null ? desiredItems.trim() : null);
			item.setUser(user);
			
			// แปลง Enum
			try {
				item.setItemCondition(ItemCondition.valueOf(itemConditionStr));
			} catch (IllegalArgumentException e) {
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "สภาพสิ่งของไม่ถูกต้อง");
				return "post";
			}
			
			try {
				item.setListingType(ListingType.valueOf(listingTypeStr));
			} catch (IllegalArgumentException e) {
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ประเภทการลงประกาศไม่ถูกต้อง");
				return "post";
			}
			
			// ดึง Category
			Category category = categoryService.getCategoryById(categoryId);
			if (category == null) {
				model.addAttribute("item", new Item());
				model.addAttribute("categories", categoryService.getAllCategories());
				model.addAttribute("errorMessage", "ไม่พบหมวดหมู่ที่เลือก");
				return "post";
			}
			item.setCategory(category);

			// บันทึกข้อมูล
			itemService.saveItemWithImages(item, thumbnail, images);

			redirectAttributes.addFlashAttribute("successMessage", "🎉 เพิ่มสิ่งของเรียบร้อยแล้ว!");
			return "redirect:/items/post";

		} catch (Exception e) {
			model.addAttribute("item", new Item());
			model.addAttribute("categories", categoryService.getAllCategories());
			model.addAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
			return "post";
		}
	}
}