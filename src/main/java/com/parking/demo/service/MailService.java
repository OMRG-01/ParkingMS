package com.parking.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendProfileUpdateEmail(String toEmail, String userName, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setTo(toEmail);
        message.setSubject("Profile Update Confirmation");
        
        // Constructing the email body
        String emailBody = "Hello " + userName + ",\n\n"
            + "Your profile has been successfully updated!\n\n"
            + "Here are your updated details:\n"
            + "Email: " + toEmail + "\n"
            + "Password: " + newPassword + "\n\n"
            + "Best regards,\nThe ParkEasy Team";
        
        message.setText(emailBody);

        // Send the email
        mailSender.send(message);
    }
}
