package com.parking.demo.controller;

import com.parking.demo.model.ParkingArea;
import com.parking.demo.repository.ParkingAreaRepository;
import com.parking.demo.service.ParkingAreaService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/admin/parking")
public class ParkingAreaController {

    @Autowired
    private ParkingAreaService parkingAreaService;
    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    // Show the parking form to add a new parking area
    @GetMapping("/add")
    public String showAddParkingForm(Model model) {
        model.addAttribute("parkingArea", new ParkingArea());
        return "admin/parking"; // parking.html inside templates/admin/
    }

    // Add a new parking area (handle POST request from the form)
    @PostMapping("/add")
    public String addParkingArea(@ModelAttribute ParkingArea parkingArea, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/parking"; // Form validation errors
        }

        try {
            parkingAreaService.addParkingArea(parkingArea);
            model.addAttribute("successMessage", "Parking Area added successfully!");
            model.addAttribute("parkingArea", new ParkingArea()); // Reset form
            return "admin/parking"; // Stay on same page
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/parking";
        }
    }


    // View all parking areas (admin dashboard)
    @GetMapping("/all")
    public String viewAllParkingAreas(Model model) {
        model.addAttribute("parkingAreas", parkingAreaService.getAllParkingAreas());
        return "admin/parkingList"; // Show the list of all parking areas
    }
    

    @GetMapping("/delete/{id}")
    public String deleteParkingArea(@PathVariable Long id) {
        parkingAreaService.softDeleteParkingArea(id);
        return "redirect:/admin/parking/all";
    }
    
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        ParkingArea parkingArea = parkingAreaService.getParkingAreaById(id);
        model.addAttribute("parkingArea", parkingArea);
        return "admin/updateParking";
    }
    
    @PostMapping("/update")
    public String updateParkingArea(@ModelAttribute ParkingArea parkingArea) {
        parkingAreaService.updateParkingArea(parkingArea);
        return "redirect:/admin/parking/all";
    }
    
    

    

}
