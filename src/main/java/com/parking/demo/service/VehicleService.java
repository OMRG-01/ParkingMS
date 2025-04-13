package com.parking.demo.service;

import com.parking.demo.model.User;
import com.parking.demo.model.Vehicle;
import com.parking.demo.repository.UserRepository;
import com.parking.demo.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    // Add vehicle to a user
    public Vehicle addVehicle(Long userId, Vehicle vehicle) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        vehicle.setUser(user);
        return vehicleRepository.save(vehicle);
    }

    // Get all vehicles for a specific user
    public List<Vehicle> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }
    
 // Method to get Vehicle by ID
    public Vehicle getVehicleById(Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);  // Finding vehicle by ID
        if (vehicle.isPresent()) {
            return vehicle.get();  // Return the vehicle if found
        } else {
            throw new RuntimeException("Vehicle not found");  // Exception if not found
        }
    }
}
