package com.parking.demo.model;

import jakarta.persistence.*;

@Entity
public class ParkingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_area_id", nullable = false)
    private ParkingArea parkingArea;

    private Double carRate;
    private Double bikeRate;

    public ParkingRate() {}

    public ParkingRate(ParkingArea parkingArea, Double carRate, Double bikeRate) {
        this.parkingArea = parkingArea;
        this.carRate = carRate;
        this.bikeRate = bikeRate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingArea getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(ParkingArea parkingArea) {
        this.parkingArea = parkingArea;
    }

    public Double getCarRate() {
        return carRate;
    }

    public void setCarRate(Double carRate) {
        this.carRate = carRate;
    }

    public Double getBikeRate() {
        return bikeRate;
    }

    public void setBikeRate(Double bikeRate) {
        this.bikeRate = bikeRate;
    }
}
