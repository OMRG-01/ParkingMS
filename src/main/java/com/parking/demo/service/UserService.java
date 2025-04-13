package com.parking.demo.service;

import com.parking.demo.model.*;
import java.util.List;

public interface UserService {
    User findByEmailAndPassword(String email, String password);
    User saveUser(User user);
    List<User> getAllUsers();
    User findById(Long id);
    void deleteUser(Long id);
    User getUserById(Long id);
    void save(User user);
    void deleteUserById(Long id);
    public void updateUser(User updatedUser);
    public String getUserEmailById(Long userId);
    public long countAllUsers();
}
