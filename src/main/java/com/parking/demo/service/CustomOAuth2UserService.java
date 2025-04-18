package com.parking.demo.service;

import com.parking.demo.model.User;
import com.parking.demo.model.Role;
import com.parking.demo.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;

        if (userOpt.isEmpty()) {
            // ➕ First-time login - create user
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setMobileNumber("0000000000");
            user.setCity("Unknown");
            user.setGender("N/A");
            user.setActive(true);

            // ✅ Generate random password
            String randomPassword = UUID.randomUUID().toString().substring(0, 8);
            user.setPassword(randomPassword);

            // ✅ Set default role
            user.setRole(new Role(2L, "USER")); // Ensure this exists in DB

            // Save the new user
            user = userRepository.save(user);

            System.out.println("✅ New Google user created: " + email);
        } else {
            user = userOpt.get();
            System.out.println("✅ Existing Google user logged in: " + email);
        }

        // ✅ Store user in session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole().getRoleName());
        }

        return oAuth2User;
    }

}
