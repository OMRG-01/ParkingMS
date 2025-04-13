package com.parking.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.parking.demo.model.*;
import com.parking.demo.repository.BookingRepository;
import com.parking.demo.service.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/book")
public class BookingController {

    @Autowired
    private ParkingAreaService parkingAreaService;

    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private ParkingRateService parkingRateService;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private NotificationService notificationService;

    
    @PostMapping("/update-payment-status")
    @ResponseBody
    public String updatePaymentStatus(@RequestParam Long bookingId) {
        // Fetch the booking by ID
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "Booking not found";

        // Update the payment status
        booking.setPaymentStatus(1); // Set as paid
        bookingService.save(booking);

        // Trigger a notification for the successful payment and booking
        notificationService.addNotification("New booking made by user: " + booking.getUser().getName() + 
                                            " for parking area: " + booking.getParkingArea().getLocation() + 
                                            " in slot: " + booking.getSlot());

        return "Payment successful";
    }

    @GetMapping("/get-booking-id")
    @ResponseBody
    public Long getBookingId(HttpSession session) {
        return (Long) session.getAttribute("bookingId");
    }

    
    // Show booking form
    @GetMapping
    public String showBookingForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login1";

        // Fetch distinct cities from ParkingArea table
        List<String> cities = parkingAreaService.getDistinctCities();
        model.addAttribute("cities", cities);

        // Fetch user's vehicles
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(user.getId());
        model.addAttribute("vehicles", vehicles);
        
        model.addAttribute("userId", user.getId());

        return "user/booking"; // booking.html inside /templates/user
    }

    // AJAX: Fetch parking areas by selected city
    @ResponseBody
    @GetMapping("/parking-by-city")
    public List<ParkingArea> getParkingByCity(@RequestParam String city) {
        return parkingAreaService.getParkingByCity(city);
    }
    
    @ResponseBody
    @GetMapping("/parking-details")
    public Map<String, Object> getParkingDetails(@RequestParam Long parkingId) {
        ParkingArea parking = parkingAreaService.getParkingAreaById(parkingId);
        Optional<ParkingRate> rate = parkingRateService.getRateByParkingArea(parking); // Adjusting to Optional

        Map<String, Object> response = new HashMap<>();
        response.put("carSlots", parking.getCarSlots());
        response.put("bikeSlots", parking.getBikeSlots());
        response.put("capacity", parking.getCapacity());

        // Fetch active bookings
        List<Booking> activeBookings = bookingService.getActiveBookingsByParkingArea(parkingId);
        List<String> bookedSlots = activeBookings.stream()
                .map(Booking::getSlot)
                .collect(Collectors.toList());

        response.put("bookedSlots", bookedSlots);

        // ✅ Null-safe access to rates
        if (rate.isPresent()) {
            ParkingRate r = rate.get();
            response.put("carRate", r.getCarRate() != null ? r.getCarRate() : 0);
            response.put("bikeRate", r.getBikeRate() != null ? r.getBikeRate() : 0);
        } else {
            response.put("carRate", 0);
            response.put("bikeRate", 0);
        }

        return response;
    }


    // Fetch vehicle type
    @ResponseBody
    @GetMapping("/vehicle-type")
    public Map<String, String> getVehicleType(@RequestParam Long vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);  // Assuming method exists in VehicleService
        Map<String, String> response = new HashMap<>();
        response.put("type", vehicle.getType());  // Returns 'Car' or 'Bike'
        return response;
    }


    
    @PostMapping("/submit")
    @ResponseBody
    public Map<String, Object> handleBookingSubmission(
            @RequestParam Long userId,
            @RequestParam Long vehicleId,
            @RequestParam Long parkingId,
            @RequestParam int hours,
            @RequestParam String selectedSlot
            
    ) {
        Booking booking = bookingService.createBooking(userId, parkingId, vehicleId, hours, selectedSlot);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Slot booked successfully!");
        response.put("bookingId", booking.getId()); // optional, useful if needed
        return response;
    }

    @GetMapping("/gateway")
    public String showGatewayPage(@RequestParam Long bookingId, Model model, HttpSession session) {
        model.addAttribute("bookingId", bookingId);
        session.setAttribute("bookingId", bookingId); // ✅ FIXED HERE
        return "user/gateway";
    }

    @GetMapping("/bookings")
    public String viewBookingHistory(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login1"; // Redirect to login if the user is not logged in
        }

        // Fetch bookings by the logged-in user
        List<Booking> bookings = bookingRepository.findByUserId(loggedInUser.getId());

        // Add vehicle details and slot to each booking
        for (Booking booking : bookings) {
            // Ensure vehicle is loaded in the Booking object
            Vehicle vehicle = booking.getVehicle(); // Access the Vehicle object directly from the Booking
            booking.setVehicle(vehicle); // This step is optional, as the Vehicle is already set in the Booking entity

            // Slot is already available, no need to set it again
        }

        model.addAttribute("bookings", bookings);
        return "user/userBooking"; // Correct the template path to the userBooking.html
    }





}
