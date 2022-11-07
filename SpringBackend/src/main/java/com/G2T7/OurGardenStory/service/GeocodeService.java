package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.geocoder.GeocodeDistance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.IOException;

@Service
public class GeocodeService {

    @Value("${geocoder.resource}")
    private String GEOCODING_RESOURCE;

    @Value("${geocoder.api-key}")
    private String API_KEY;

    HashMap<String, Double> map = new HashMap<>();

    /**
    * Calls the distanceCalculator method which calculates the distance between a user's address and the garden's address
    *
    * @param username a String
    * @param userAddress a String
    * @param gardenLng the longitude of the Garden's address
    * @param gardenLat the latitude of the Garden's address
    * @return the distance in km
    */
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