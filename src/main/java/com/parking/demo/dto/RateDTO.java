package com.parking.demo.dto;

public class RateDTO {
    private Long parkingAreaId;
    private Double carRate;
    private Double bikeRate;

    public Long getParkingAreaId() {
        return parkingAreaId;
    }

    public void setParkingAreaId(Long parkingAreaId) {
        this.parkingAreaId = parkingAreaId;
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
