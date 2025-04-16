package com.parking.demo.controller;

import com.parking.demo.*;
import com.parking.demo.model.*;
import com.parking.demo.repository.*;
import com.parking.demo.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired 
    private RoleService roleService;
    
    @Autowired
    private VehicleService vehicleService;
   
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ParkingAreaService parkingAreaService;

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @GetMapping("/")
    public String home() {
        return "index"; // Redirects to index.html in 'static'
    }
    @GetMapping("/login1")
    public String showLoginPage() {
        return "login"; // This remains the same
    }
    @GetMapping("/admin/dash")
    public ModelAndView checkAdminLogin(HttpSession session) {
        Object user = session.getAttribute("loggedInUser"); // Adjust this key to your project

        if (user != null) {
            // User is logged in
            return new ModelAndView("admin/adminDash"); // Goes to admin/adminDash.html
        } else {
            // User not logged in, redirect to login controller
            return new ModelAndView("redirect:/login1");
        }
    }
    
    @GetMapping("/admin/api/today-revenue")
    @ResponseBody
    public double getTodayRevenueAjax() {
        List<Booking> paidBookings = bookingRepository.findByPaymentStatus(1);
        LocalDate today = LocalDate.now();

        double totalTodayRevenue = paidBookings.stream()
            .filter(booking -> booking.getBookingTime().toLocalDate().isEqual(today))
            .mapToDouble(Booking::getAmount)
            .sum();

        return totalTodayRevenue;
    }


    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password, 
                        HttpServletRequest request, 
                        Model model,
                        HttpServletResponse response) {

        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Email and password are required");
            return "login"; // Return login.html with error
        }

        try {
            User user = userRepository.findByEmailAndPassword(email, password);

            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole().getRoleName());
                session.setMaxInactiveInterval(30 * 60);

                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("X-Content-Type-Options", "nosniff");

                return user.getRole().getRoleName().equalsIgnoreCase("ADMIN") ?
                    "redirect:/admin/dashboard" : "redirect:/user/dashboard";
            } else {
                Thread.sleep(1000); // Delay to prevent brute force
                model.addAttribute("error", "Invalid email or password");
                return "login"; // Stay on login page with error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again.");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
        return "redirect:/login1";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login1"; // Redirect if not logged in
        }

        List<Vehicle> userVehicles = vehicleService.getVehiclesByUser(loggedInUser.getId());
        model.addAttribute("vehicles", userVehicles);

        return "user/userDash";
    }


   

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if session exists
        if (loggedInUser == null) {
            return "redirect:/login1"; // Redirect if not logged in
        }

        // Ensure user has ADMIN role
        if (!"ADMIN".equals(loggedInUser.getRole().getRoleName())) {
            return "redirect:/user/dashboard"; // Redirect non-admin users
        }

        // Fetch statistics for the dashboard
        long totalParkingAreas = parkingAreaService.countAllParkingAreas();
        long activeBookings = bookingService.countActiveBookings();
        long totalUsers = userService.countAllUsers();

        // Calculate changes and additional statistics

        // Add all attributes to the model to pass to the view
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("totalParkingAreas", totalParkingAreas);
        model.addAttribute("activeBookings", activeBookings);
        model.addAttribute("totalUsers", totalUsers);

        return "admin/adminDash"; // Return to the admin dashboard view
    }

    
    @GetMapping("/register1")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";  // The path to your register.html page
    }

    // Handle the registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {

        // Fetch the "USER" role (role_id = 2)
        Role userRole = roleService.findById(2L);

        // Set the role to "USER"
        user.setRole(userRole);

        // Save the user to the database
        userService.save(user);

        // Trigger a notification for the new user registration
        notificationService.addNotification("New user registered: " + user.getName());

        return "redirect:/register1";  // Redirect to the login page after successful registration
    }


}
