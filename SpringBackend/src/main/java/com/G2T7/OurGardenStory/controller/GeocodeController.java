package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.geocoder.GeocodeDistance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.io.IOException;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class GeocodeController {

    @Value("${geocoder.resource}")
    private String GEOCODING_RESOURCE;

    @Value("${geocoder.api-key}")
    private String API_KEY;

    HashMap<String, Double> map = new HashMap<>();

    @PostMapping(path = "/geocode")
    public double saveDistance(String username, String userAddress, String gardenLng, String gardenLat) {
        double distance = 0.0;
        try {
            distance = GeocodeDistance.distanceCalculator(userAddress, gardenLat, gardenLng, GEOCODING_RESOURCE,
                    API_KEY);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        map.put(username, distance);

        return distance;
    }
}