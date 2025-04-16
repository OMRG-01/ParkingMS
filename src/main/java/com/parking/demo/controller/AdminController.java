package com.parking.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.parking.demo.model.Booking;
import com.parking.demo.model.Role;
import com.parking.demo.model.User;
import com.parking.demo.repository.BookingRepository;
import com.parking.demo.repository.RoleRepository;
import com.parking.demo.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoleRepository roleRepository; // assuming you have this

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<User> users = userRepository.findByRole_Id(2); // Role ID 2 = regular users
        model.addAttribute("users", users);
        return "admin/userView";
    }

    
    @GetMapping("/booking/{userId}")
    @ResponseBody
    public Map<String, Object> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        double totalPaid = bookings.stream()
            .filter(b -> b.getPaymentStatus() == 1)
            .mapToDouble(Booking::getAmount)
            .sum();

        double totalUnpaid = bookings.stream()
            .filter(b -> b.getPaymentStatus() == 0)
            .mapToDouble(Booking::getAmount)
            .sum();

        Map<String, Object> response = new HashMap<>();
        response.put("bookings", bookings);
        response.put("totalPaid", totalPaid);
        response.put("totalUnpaid", totalUnpaid);
        
        return response;
    }
    
    @GetMapping("/reports")
    public String viewReportPage(Model model) {
        List<Booking> bookings = bookingRepository.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin/report";
    }


    @GetMapping("/bookings/report")
    @ResponseBody
    public List<Booking> filterBookings(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer paymentStatus,
            @RequestParam(required = false) String userEmail
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime from = (fromDate != null && !fromDate.isEmpty())
                ? LocalDate.parse(fromDate, formatter).atStartOfDay()
                : null;
        LocalDateTime to = (toDate != null && !toDate.isEmpty())
                ? LocalDate.parse(toDate, formatter).atTime(23, 59, 59)
                : null;

        return bookingRepository.findFilteredBookings(from, to, paymentStatus, userEmail);
    }


}
