package com.parking.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.parking.demo.model.User;
import com.parking.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OAuth2LoginController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/dashboard")
    public String handleLoginSuccess(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        // ✅ SAFELY try to get oauth2User from session
        if (session != null) {
            User user = (User) session.getAttribute("loggedInUser");
            if (user != null) {
                model.addAttribute("user", user);
                return "user/userDash"; // ✅ correct Thymeleaf view
            }
        }

        // If not logged in, redirect to login page
        return "redirect:/login1";
    }


}
