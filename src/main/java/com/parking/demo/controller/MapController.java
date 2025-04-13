package com.parking.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.parking.demo.model.ParkingArea;
import com.parking.demo.repository.ParkingAreaRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class MapController {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    @GetMapping("/api/parking-locations")
    public List<Map<String, Object>> getParkingLocations() {
        List<ParkingArea> parkingAreas = parkingAreaRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        for (ParkingArea pa : parkingAreas) {
            try {
                String encodedLocation = URLEncoder.encode(pa.getLocation(), StandardCharsets.UTF_8);
                String geocodeUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedLocation;

                HttpHeaders headers = new HttpHeaders();
                headers.set("User-Agent", "ParkingApp/1.0 (your@email.com)");
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<List> response = restTemplate.exchange(
                        geocodeUrl,
                        HttpMethod.GET,
                        entity,
                        List.class
                );

                if (response.getBody() != null && !response.getBody().isEmpty()) {
                    LinkedHashMap firstResult = (LinkedHashMap) response.getBody().get(0);
                    Map<String, Object> locMap = new HashMap<>();
                    locMap.put("name", pa.getName());
                    locMap.put("location", pa.getLocation());
                    locMap.put("lat", firstResult.get("lat"));
                    locMap.put("lon", firstResult.get("lon"));
                    result.add(locMap);
                }

            } catch (Exception e) {
                e.printStackTrace(); // log error
            }
        }

        return result;
    }
}
