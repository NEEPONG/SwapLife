package com.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.model.User;
import com.springboot.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            model.addAttribute("success", true);
            model.addAttribute("user", new User());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }
    
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) MultipartFile profileImage,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            userService.updateUserProfile(username, firstName, lastName, profileImage);
            redirectAttributes.addAttribute("updateSuccess", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("updateError", e.getMessage());
        }

        return "redirect:/profile";
    }
    
    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        try {
            userService.changePassword(username, oldPassword, newPassword, confirmPassword);
            redirectAttributes.addAttribute("success", true);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

}
