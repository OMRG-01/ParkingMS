package com.parking.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.parking.demo.model.Booking;
import com.parking.demo.model.User;
import com.parking.demo.repository.BookingRepository;
import com.parking.demo.service.OTPService;
import com.parking.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserService userService; // A service to fetch user data

    @Autowired
    private OTPService otpService; // A service to generate and store OTPs
    
    

    private static final int OTP_LENGTH = 6;
    
    
    // Endpoint to send OTP to user email
    @PostMapping("/send-otp")
    @ResponseBody
    public String sendOTP(@RequestParam("userId") Long userId, HttpSession session) {
        // Fetch the user's email from the database using the userId
    	String userEmail = userService.getUserEmailById(userId); // Fetch email based on userId
        if (userEmail == null) {
            return "User not found";
        }

        // Generate OTP
        String otp = otpService.generateOTP();

        // Send OTP to user's email
        sendOtpEmail(userEmail, otp);

        // Store OTP in session or database for later verification
        session.setAttribute("otp", otp); // Storing OTP in session for simplicity

        return "OTP sent to email";
    }

    // Method to send OTP email
    private void sendOtpEmail(String userEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Your OTP for Parking Slot Booking");
        message.setText("Your OTP is: " + otp);

        emailSender.send(message);
    }

    // Endpoint to verify OTP
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("otp") String otp, @RequestParam("userId") String userId, HttpSession session) {
        String storedOtp = (String) session.getAttribute("otp"); // Get OTP stored in session

        // Compare entered OTP with stored OTP
        if (storedOtp != null && storedOtp.equals(otp)) {
            return "OTP verified successfully";
        } else {
            return "Invalid OTP";
        }
    }
    
    
}
