package com.parking.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.demo.model.*;
import com.parking.demo.repository.ParkingRateRepository;

@Service
public class ParkingRateService {

    @Autowired
    private ParkingRateRepository parkingRateRepository;

    public Optional<ParkingRate> getRateByParkingArea(ParkingArea area) {
        return parkingRateRepository.findByParkingArea(area);
    }


    public void save(ParkingRate rate) {
        parkingRateRepository.save(rate);
    }
}
