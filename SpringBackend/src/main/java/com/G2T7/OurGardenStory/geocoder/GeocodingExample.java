package com.G2T7.OurGardenStory.geocoder;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeocodingExample {

    public static double distanceCalculator(String userAddress, String gardenLat, String gardenLng, String resource, String apiKey) throws IOException, InterruptedException {
        String userLat = "";
        String userLng = "";

        ObjectMapper mapper = new ObjectMapper();
        Geocoder geocoder = new Geocoder();

        String response = geocoder.GeocodeSync(userAddress, resource, apiKey);
        JsonNode responseJsonNode = mapper.readTree(response);
        JsonNode items = responseJsonNode.get("items");

        for (JsonNode item : items) {
            JsonNode address = item.get("address");
            String label = address.get("label").asText();
            JsonNode position = item.get("position");

            userLat = position.get("lat").asText();
            userLng = position.get("lng").asText();
            System.out.println(label + " is located at " + userLat + "," + userLng + ".");
        }

        double result = distanceBetweenTwoPoints(userLat, userLng, gardenLat, gardenLng);
        System.out.println("The distance between the two addresses is " + result + "km");
        return result;

    }

    public static double distanceBetweenTwoPoints(String lat1, String lng1, String lat2, String lng2) {
        double latitude1 = Double.parseDouble(lat1);
        double longitude1 = Double.parseDouble(lng1);
        double latitude2 = Double.parseDouble(lat2);
        double longitude2 = Double.parseDouble(lng2);

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        longitude1 = Math.toRadians(longitude1);
        longitude2 = Math.toRadians(longitude2);
        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        // Haversine formula
        double dlon = longitude1 - longitude2;
        double dlat = latitude1 - latitude2;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(latitude1) * Math.cos(latitude2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r * 1.609344);
    }
}