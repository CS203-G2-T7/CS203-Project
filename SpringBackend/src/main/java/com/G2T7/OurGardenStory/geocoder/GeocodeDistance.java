package com.G2T7.OurGardenStory.geocoder;

import java.io.IOException;

import com.fasterxml.jackson.databind.*;
public class GeocodeDistance {

    //radius of earth in KM
    final static double RADIUS = 6371;
    //converts miles to KM
    final static double KILOMETER = 1.609344;

    /**
    * Calculates the distance between a User's address and the Garden's address
    *
    * @param userAddress a String
    * @param gardenLng the longitude of the Garden's address
    * @param gardenLat the latitude of the Garden's address
    * @param resource the Geocode Here api resource
    * @param apiKey the api key for the Geocode Here api
    * @return the distance between the user's address and the garden
    */
    //calculates distance between garden and user address
    public static double distanceCalculator(String userAddress, String gardenLat, String gardenLng, String resource, String apiKey) throws IOException, InterruptedException {
        String userLat = "";
        String userLng = "";

        ObjectMapper mapper = new ObjectMapper();
        Geocoder geocoder = new Geocoder();
        String response = geocoder.GeocodeSync("Singapore" + userAddress, resource, apiKey);
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

        return distanceBetweenTwoPoints(userLat, userLng, gardenLat, gardenLng);
    }

    /**
    * Calculates the distance between two points defined by their latitude and longitude
    *
    * @param lat1 latitude of first address
    * @param lng1 longitude of first address
    * @param lat2 latitude of second address
    * @param lng2 longitude of second address
    * @return the distance between the two points
    */
    private static double distanceBetweenTwoPoints(String lat1, String lng1, String lat2, String lng2) {
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
        double c = haversineFormula(longitude1, longitude2, latitude1, latitude2);

        // calculate the result
        return(c * RADIUS * KILOMETER);
    }

    private static double haversineFormula (double lng1, double lng2, double lat1, double lat2) {
        double dlon = lng1 - lng2;
        double dlat = lat1 - lat2;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        return 2 * Math.asin(Math.sqrt(a));
    }
}

    