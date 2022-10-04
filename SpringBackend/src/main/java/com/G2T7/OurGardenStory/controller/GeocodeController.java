package com.G2T7.OurGardenStory.controller;
import com.G2T7.OurGardenStory.geocoder.DistanceSorterList;
import com.G2T7.OurGardenStory.geocoder.GeocodingExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GeocodeController {

    private DistanceSorterList distancesorterlist;

    @PostMapping(path = "/geocode")
    public DistanceSorterList saveDistance(String address1, String address2) {
        double distance = 0.0;
        try {
            distance = GeocodingExample.distanceCalculator(address1, address2);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
        distancesorterlist.add(distance);
        return distancesorterlist;
    }


}
