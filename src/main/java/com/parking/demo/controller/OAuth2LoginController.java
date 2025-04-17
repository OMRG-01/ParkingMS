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
        // Get the logged-in user from the session (you can store the user's details in the session)
        OAuth2User oAuth2User = (OAuth2User) request.getAttribute("oauth2User");

        // Get the email from the OAuth2User
        String email = oAuth2User.getAttribute("email");

        // Retrieve the user from the database (you can use userRepository here)
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Set session attributes for the logged-in user
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().getRoleName());
            session.setMaxInactiveInterval(30 * 60); // Set session timeout
        }

        return "user/userDash"; // Redirect to the dashboard
    }
}
