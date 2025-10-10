package com.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
 @GetMapping("/")
 public String showHomePage(Model model) {
     
     return "index";
 }
}
