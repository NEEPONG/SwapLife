package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String showHomePage() {

		return "index";
	}
	
	@GetMapping("/login")
	public String showLoginPage() {
			
		return "login";
	}
}
