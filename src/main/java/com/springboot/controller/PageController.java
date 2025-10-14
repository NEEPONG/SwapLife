package com.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.model.Category;
import com.springboot.model.User;
import com.springboot.service.CategoryService;
import com.springboot.service.UserService;

@Controller
public class PageController {

	 	@Autowired
	    private CategoryService categoryService;
	 	
	 	@Autowired
	 	private UserService userService;

	    @GetMapping("/")
	    public String showHomePage(Model model) {
	        List<Category> popularCategories = categoryService.getPopularCategories();
	        model.addAttribute("popularCategories", popularCategories);
	        return "index"; // ✅ ชื่อไฟล์ HTML ที่จะไป render
	    }
	    
	    @GetMapping("/profile")
	    public String showProfilePage(Authentication authentication, Model model) {
	        // ✅ ดึงชื่อผู้ใช้จาก session
	        String username = authentication.getName();

	        // ✅ ดึงข้อมูลผู้ใช้จากฐานข้อมูล
	        User user = userService.findByUsername(username);

	        // ✅ ส่งข้อมูลไปยัง view
	        model.addAttribute("user", user);
	        return "profile";
	    }
}
