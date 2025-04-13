package com.parking.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parking.demo.model.ParkingArea;
import com.parking.demo.model.ParkingRate;

@Repository
public interface ParkingRateRepository extends JpaRepository<ParkingRate, Long> {
    Optional<ParkingRate> findByParkingArea(ParkingArea parkingArea);
}
