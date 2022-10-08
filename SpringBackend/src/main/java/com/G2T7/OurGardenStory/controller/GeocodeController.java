// package com.G2T7.OurGardenStory.controller;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import com.G2T7.OurGardenStory.geocoder.Geocoder;

// import java.io.IOException;

// public class GeocodeController {

//     // @Value(value = "${Geocoder_API_KEY}")
//     // private static String API_KEY;

//     // @RequestMapping(path = "/geocode", method = RequestMethod.GET)
//     // public GeocodeResult getGeocode(@RequestParam String address) throws IOException {
//     //     OkHttpClient client = new OkHttpClient();
//     //     String encodedAddress = URLEncoder.encode(address, "UTF-8");
//     //     Request request = new Request.Builder()
//     //             .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
//     //             .get()
//     //             .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
//     //             .addHeader("x-rapidapi-key", API_KEY)
//     //             .build();
//     //     ResponseBody responseBody = client.newCall(request).execute().body();
//     //     ObjectMapper objectMapper = new ObjectMapper();
//     //     GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
//     //     return result;   
//     // }

//         // @Autowired
//         // private Geocoder geocode;    

//         // @RequestMapping(path = "/geocode", method = RequestMethod.GET)
//         // public String getGarden(@PathVariable("query") String query) throws IOException, InterruptedException {
//         //     return geocode.GeocodeSync(query);
//     }
// }

package com.G2T7.OurGardenStory.controller;
//import com.G2T7.OurGardenStory.geocoder.DistanceSorterList;
import com.G2T7.OurGardenStory.geocoder.GeocodingExample;
import com.G2T7.OurGardenStory.repository.BallotRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @PostMapping (path = "/geocode")
    public double saveDistance(String username, String userAddress, String gardenLng, String gardenLat) {
        double distance = 0.0;
        try {
            distance = GeocodingExample.distanceCalculator(userAddress, gardenLat, gardenLng, GEOCODING_RESOURCE, API_KEY);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        map.put(username, distance);

        return distance;
    }
}