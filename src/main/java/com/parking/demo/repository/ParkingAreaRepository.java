package com.parking.demo.repository;

import com.parking.demo.model.ParkingArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ParkingAreaRepository extends JpaRepository<ParkingArea, Long> {

    // Get all parking areas in a specific city (optional)
    List<ParkingArea> findByCity(String city);

    // Find parking area by name
    ParkingArea findByName(String name);
    
    @Query("SELECT p FROM ParkingArea p WHERE p.isDeleted = false")
    List<ParkingArea> findAllActive();
    
    List<ParkingArea> findByIsDeletedFalse();
    
    @Query("SELECT DISTINCT p.city FROM ParkingArea p WHERE p.isDeleted = false")
    List<String> findDistinctCities();

    List<ParkingArea> findByCityAndIsDeletedFalse(String city);
    
    long count();

}
