package com.G2T7.OurGardenStory.geocoder;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeocodingExample {

    public static double distanceCalculator(String address1, String address2) throws IOException, InterruptedException {
        String lat1 = "";
        String lat2 = "";
        String lng1 = "";
        String lng2 = "";

        ObjectMapper mapper = new ObjectMapper();
        Geocoder geocoder = new Geocoder();

        String response = geocoder.GeocodeSync(address1);
        JsonNode responseJsonNode = mapper.readTree(response);
        JsonNode items = responseJsonNode.get("items");

        String response2 = geocoder.GeocodeSync(address2);
        JsonNode responseJsonNode2 = mapper.readTree(response2);
        JsonNode items2 = responseJsonNode2.get("items");

        for (JsonNode item : items) {
            JsonNode address = item.get("address");
            String label = address.get("label").asText();
            JsonNode position = item.get("position");

            lat1 = position.get("lat").asText();
            lng1 = position.get("lng").asText();
            System.out.println(label + " is located at " + lat1 + "," + lng1 + ".");
        }

        for (JsonNode item : items2) {
            JsonNode address = item.get("address");
            String label = address.get("label").asText();
            JsonNode position = item.get("position");

            lat2 = position.get("lat").asText();
            lng2 = position.get("lng").asText();
            System.out.println(label + " is located at " + lat2 + "," + lng2 + ".");
        }

        double result = distanceBetweenTwoPoints(lat1, lng1, lat2, lng2);
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
