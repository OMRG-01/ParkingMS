package com.parking.demo.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    // Generate 6-digit OTP
    public String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10)); // Generates a random digit (0-9)
        }
        return otp.toString();
    }
}
