package com.parking.demo.service;

import com.parking.demo.model.ParkingArea;
import com.parking.demo.repository.ParkingAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingAreaService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    // Add a new parking area
    public ParkingArea addParkingArea(ParkingArea parkingArea) {
        // Validation: If type is 'Both', validate that carSlots + bikeSlots = capacity
        if ("Both".equals(parkingArea.getType())) {
            if (parkingArea.getCarSlots() == null || parkingArea.getBikeSlots() == null || parkingArea.getCarSlots() + parkingArea.getBikeSlots() != parkingArea.getCapacity()) {
                throw new IllegalArgumentException("Car and Bike slots must sum to the total capacity");
            }
        } else if ("Car".equals(parkingArea.getType()) && parkingArea.getCarSlots() != parkingArea.getCapacity()) {
            throw new IllegalArgumentException("For car type, the car slots must equal the capacity");
        } else if ("Bike".equals(parkingArea.getType()) && parkingArea.getBikeSlots() != parkingArea.getCapacity()) {
            throw new IllegalArgumentException("For bike type, the bike slots must equal the capacity");
        }

        return parkingAreaRepository.save(parkingArea);
    }

    

 // In ParkingAreaService.java

    public ParkingArea getParkingAreaById(Long id) {
        return parkingAreaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parking area not found with id: " + id));
    }

    
    public void softDeleteParkingArea(Long id) {
        ParkingArea parkingArea = parkingAreaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parking area ID"));
        parkingArea.setDeleted(true);
        parkingAreaRepository.save(parkingArea);
    }
    
    public void updateParkingArea(ParkingArea parkingArea) {
        parkingAreaRepository.save(parkingArea);
    }
    
    public List<ParkingArea> getAllParkingAreas() {
        return parkingAreaRepository.findByIsDeletedFalse(); // only not-deleted
    }

    public List<String> getDistinctCities() {
        return parkingAreaRepository.findDistinctCities();
    }

    public List<ParkingArea> getParkingByCity(String city) {
        return parkingAreaRepository.findByCityAndIsDeletedFalse(city);
    }
    
    public long countAllParkingAreas() {
        return parkingAreaRepository.count();
    }

}
