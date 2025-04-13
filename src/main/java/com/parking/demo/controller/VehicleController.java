package com.parking.demo.controller;

import com.parking.demo.model.Vehicle;
import com.parking.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/user/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // 🌐 Show vehicle.html page
    @GetMapping
    public String showVehiclePage(@RequestParam("userId") Long userId, Model model) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(userId);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("userId", userId);
        return "user/vehicle"; // vehicle.html inside templates/user/
    }

    // 🆕 Add a new vehicle for a user
    @PostMapping("/add")
    public String addVehicle(@RequestParam("userId") Long userId, @ModelAttribute Vehicle vehicle) {
        vehicleService.addVehicle(userId, vehicle);
        return "redirect:/user/vehicle?userId=" + userId;
    }
}
