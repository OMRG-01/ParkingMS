package com.parking.demo.repository;

import com.parking.demo.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Get all vehicles by user ID
    List<Vehicle> findByUserId(Long userId);
}
