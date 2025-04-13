package com.parking.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parking_area_id", nullable = false)
    private ParkingArea parkingArea;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime; // Current timestamp

    @Column(name = "booked_until", nullable = false)
    private LocalDateTime bookedUntil; // bookingTime + hours

    @Column(name = "slot", nullable = false)
    private String slot; // e.g., B1 or C2

    @Column(name = "payment_status")
    private int paymentStatus;  // 0 = not paid, 1 = paid

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Default Constructor
    public Booking() {
    }

    // Parameterized Constructor
    public Booking(User user, ParkingArea parkingArea, Vehicle vehicle, LocalDateTime bookingTime, LocalDateTime bookedUntil, String slot) {
        this.user = user;
        this.parkingArea = parkingArea;
        this.vehicle = vehicle;
        this.bookingTime = bookingTime;
        this.bookedUntil = bookedUntil;
        this.slot = slot;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public ParkingArea getParkingArea() {
        return parkingArea;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public LocalDateTime getBookedUntil() {
        return bookedUntil;
    }

    public String getSlot() {
        return slot;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setParkingArea(ParkingArea parkingArea) {
        this.parkingArea = parkingArea;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setBookedUntil(LocalDateTime bookedUntil) {
        this.bookedUntil = bookedUntil;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}