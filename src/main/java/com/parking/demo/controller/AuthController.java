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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            // Check if user exists with the given email and password
            User user = userRepository.findByEmailAndPassword(email, password);

            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("loggedInUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole().getRoleName());
                session.setMaxInactiveInterval(30 * 60);  // Session timeout

                // Set headers to prevent caching
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("X-Content-Type-Options", "nosniff");

                // Redirect to appropriate dashboard based on user role
                if (user.getRole().getRoleName().equalsIgnoreCase("ADMIN")) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/user/dashboard";
                }
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "login";  // Stay on login page with error message
            }
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred. Please try again.");
            return "login";  // Stay on login page with error message
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
        return "redirect:/login1";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        // Get the logged-in user from session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            // If not logged in, redirect to login page
            return "redirect:/login1"; 
        }

        // Check if the mobile number is still the default (00000000)
        if ("0000000000".equals(loggedInUser.getMobileNumber())) {
            // Redirect to profile update page if the mobile number is the default
            return "redirect:/update-profile";
        }

        // Fetch user's vehicles for the dashboard
        List<Vehicle> userVehicles = vehicleService.getVehiclesByUser(loggedInUser.getId());
        model.addAttribute("vehicles", userVehicles);

        return "user/userDash"; // Display user dashboard
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

        return "redirect:/login1";  // Redirect to the login page after successful registration
    }

    @GetMapping("/update-profile")
    public String showUpdateProfilePage(HttpSession session, Model model) {
        // Get the logged-in user from session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login1"; // If not logged in, redirect to login page
        }

        // Add logged-in user to model for editing
        model.addAttribute("user", loggedInUser);

        return "user/updateProfile"; // Return the update profile page
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("user") User updatedUser, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login1"; // If not logged in, redirect to login page
        }

        // If the user has updated their password, set it
        if (!updatedUser.getPassword().isEmpty()) {
            loggedInUser.setPassword(updatedUser.getPassword());  // You might want to add password validation and encryption here
        }

        // Update other fields (like name, mobile, etc.)
        loggedInUser.setName(updatedUser.getName());
        loggedInUser.setMobileNumber(updatedUser.getMobileNumber());
        loggedInUser.setCity(updatedUser.getCity());
        loggedInUser.setGender(updatedUser.getGender());

        // Save updated user to the database
        userService.updateUser(loggedInUser);

        // Update the session with the new user data
        session.setAttribute("loggedInUser", loggedInUser);

        // Add success message and redirect
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/user/dashboard"; // Redirect to the dashboard after updating
    }
}
