package com.parking.demo.service;

import com.parking.demo.model.User;
import com.parking.demo.repository.UserRepository;
import com.parking.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MailService mailService;
    
    @Override
    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public void save(User user) {
        userRepository.save(user);  // Save the user to the database
    }
    public List<User> getAllUsers() {
        return userRepository.findByRoleId(2); // Only users with role 'USER' (role_id = 2)
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    // Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId()).orElse(null);
        
        if (existingUser != null) {
            // Update user details
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setMobileNumber(updatedUser.getMobileNumber());
            existingUser.setCity(updatedUser.getCity());
            existingUser.setGender(updatedUser.getGender());

            // If the password is not empty and the user wants to change it
            String newPassword = updatedUser.getPassword();
            if (newPassword != null && !newPassword.isEmpty()) {
                existingUser.setPassword(newPassword);  // Set the new password (no encryption)
            } else {
                newPassword = existingUser.getPassword();  // If no new password is provided, keep the old one
            }

            // Save the updated user to the database
            userRepository.save(existingUser);
            
         // Send the email confirmation with the updated password
            mailService.sendProfileUpdateEmail(existingUser.getEmail(), existingUser.getName(), newPassword);
        }
    }
        
    public String getUserEmailById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getEmail() : null;
    }

    public long countAllUsers() {
        return userRepository.count();
    }

}
