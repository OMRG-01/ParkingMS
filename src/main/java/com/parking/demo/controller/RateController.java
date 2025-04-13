package com.parking.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.parking.demo.dto.RateDTO;
import com.parking.demo.model.*;
import com.parking.demo.service.*;

@Controller
@RequestMapping("/admin")
public class RateController {

    @Autowired
    private ParkingAreaService parkingAreaService;

    @Autowired
    private ParkingRateService parkingRateService;

    // Show the page where admin can manage parking rates
    @GetMapping("/rates")
    public String showRatesPage(Model model) {
        List<ParkingArea> parkingAreas = parkingAreaService.getAllParkingAreas();
        
        // Add existing rates to parking areas if available
        parkingAreas.forEach(area -> {
        	parkingRateService.getRateByParkingArea(area)
            .ifPresent(area::setParkingRate); // Only set if present

        });

        model.addAttribute("parkingAreas", parkingAreas);
        return "admin/rate"; // This will resolve to rate.html
    }

//    @PostMapping("/rates/save")
//    public String saveRates(
//            @RequestParam List<Long> parkingAreaIds,
//            @RequestParam(required = false) List<Double> carRates,
//            @RequestParam(required = false) List<Double> bikeRates
//    ) {
//        for (int i = 0; i < parkingAreaIds.size(); i++) {
//            Long areaId = parkingAreaIds.get(i);
//            Double carRate = (carRates != null && i < carRates.size()) ? carRates.get(i) : null;
//            Double bikeRate = (bikeRates != null && i < bikeRates.size()) ? bikeRates.get(i) : null;
//
//            ParkingArea area = parkingAreaService.getParkingAreaById(areaId);
//
//            ParkingRate rate = parkingRateService.getRateByParkingArea(area)
//                    .orElse(new ParkingRate(area, carRate, bikeRate));
//
//            if (area.getType().equalsIgnoreCase("Car")) {
//                rate.setCarRate(carRate);
//                rate.setBikeRate(null);
//            } else if (area.getType().equalsIgnoreCase("Bike")) {
//                rate.setBikeRate(bikeRate);
//                rate.setCarRate(null);
//            } else {
//                rate.setCarRate(carRate);
//                rate.setBikeRate(bikeRate);
//            }
//
//            parkingRateService.save(rate);
//        }
//
//        return "redirect:/admin/rates?success";
//    }
    @PostMapping("/rates/save/ajax")
    @ResponseBody
    public String saveRatesAjax(@RequestBody List<RateDTO> rates) {
        for (RateDTO dto : rates) {
            ParkingArea area = parkingAreaService.getParkingAreaById(dto.getParkingAreaId());

            ParkingRate rate = parkingRateService.getRateByParkingArea(area)
                    .orElse(new ParkingRate(area, dto.getCarRate(), dto.getBikeRate()));

            if ("Car".equalsIgnoreCase(area.getType())) {
                rate.setCarRate(dto.getCarRate());
                rate.setBikeRate(null);
            } else if ("Bike".equalsIgnoreCase(area.getType())) {
                rate.setBikeRate(dto.getBikeRate());
                rate.setCarRate(null);
            } else {
                rate.setCarRate(dto.getCarRate());
                rate.setBikeRate(dto.getBikeRate());
            }

            parkingRateService.save(rate);
        }

        return "OK";
    }


}
