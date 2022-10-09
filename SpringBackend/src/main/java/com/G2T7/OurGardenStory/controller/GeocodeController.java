package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.geocoder.GeocodingExample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.io.IOException;

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
            distance = GeocodingExample.distanceCalculator(userAddress, gardenLat, gardenLng, GEOCODING_RESOURCE,
                    API_KEY);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        map.put(username, distance);

        return distance;
    }
}