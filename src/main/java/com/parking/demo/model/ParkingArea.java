package com.parking.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_area")
public class ParkingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String type; // "Car", "Bike", or "Both"

    private Integer carSlots;  // Will be null if type is "Bike"
    private Integer bikeSlots; // Will be null if type is "Car"

    @Column(nullable = false)
    private String ownerName;
      // Added email field for the parking owner

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
    
    @Transient
    private ParkingRate parkingRate; 
    
    public ParkingArea() {}

    public ParkingArea(Long id, String name, String location, String city, Integer capacity, String type, Integer carSlots, Integer bikeSlots, String ownerName, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.city = city;
        this.capacity = capacity;
        this.type = type;
        this.carSlots = carSlots;
        this.bikeSlots = bikeSlots;
        this.ownerName = ownerName;
        this.isDeleted = isDeleted;
    }
    
   // This is transient because it's not saved directly in the database

    // Other fields and methods

    public ParkingRate getParkingRate() {
        return parkingRate;
    }

    public void setParkingRate(ParkingRate parkingRate) {
        this.parkingRate = parkingRate;
    }
    public boolean isDeleted() {
        return isDeleted;
    }

    // Setter for isDeleted
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCarSlots() {
        return carSlots;
    }

    public void setCarSlots(Integer carSlots) {
        this.carSlots = carSlots;
    }

    public Integer getBikeSlots() {
        return bikeSlots;
    }

    public void setBikeSlots(Integer bikeSlots) {
        this.bikeSlots = bikeSlots;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
