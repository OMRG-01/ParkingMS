package com.parking.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.demo.model.*;
import com.parking.demo.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Booking> getTodayBookings() {
        return bookingRepository.findTodayBookings();
    }

    
    public Booking createBooking(Long userId, Long parkingAreaId, Long vehicleId, int hours, String selectedSlot, double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ParkingArea parkingArea = parkingAreaRepository.findById(parkingAreaId)
                .orElseThrow(() -> new RuntimeException("Parking Area not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        LocalDateTime bookingTime = LocalDateTime.now();
        LocalDateTime bookedUntil = bookingTime.plusHours(hours);
        
        
        Booking booking = new Booking();
        booking.setAmount(amount);
        booking.setUser(user);
        booking.setParkingArea(parkingArea);
        booking.setVehicle(vehicle);
        booking.setBookingTime(bookingTime);
        booking.setBookedUntil(bookedUntil);
        booking.setSlot(selectedSlot);

        return bookingRepository.save(booking);
    }
    
    public boolean isSlotAvailable(Long parkingAreaId, String slot) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findActiveBookingForSlot(parkingAreaId, slot, now);
        return bookings.isEmpty();
    }
    
    public List<Booking> getActiveBookingsByParkingArea(Long parkingAreaId) {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByParkingAreaIdAndBookedUntilAfter(parkingAreaId, now);
    }
    
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    // Count all active bookings (where the booking is not yet completed)
    public long countActiveBookings() {
        return bookingRepository.countByBookedUntilAfter(LocalDateTime.now());
    }

}
