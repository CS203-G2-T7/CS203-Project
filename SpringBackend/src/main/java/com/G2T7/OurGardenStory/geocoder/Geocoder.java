package com.G2T7.OurGardenStory.geocoder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;

public class Geocoder {

    public String GeocodeSync(String query, String resource, String apiKey) throws IOException, InterruptedException {
        // System.out.println("API_KEY = " + API_KEY);
        HttpClient httpClient = HttpClient.newHttpClient();

        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = resource + "?apiKey=" + apiKey + "&q=" + encodedQuery;
        URI uri = URI.create(requestUri);
        System.out.println("request uri = " + requestUri);
        System.out.println("uri = " + uri);

        // HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
        //         .timeout(Duration.ofMillis(2000)).build();

        HttpRequest.Builder geocodingRequest1 = HttpRequest.newBuilder().GET();
        System.out.println(geocodingRequest1);
        HttpRequest.Builder geocodingRequest2 = geocodingRequest1.uri(uri);
        System.out.println(geocodingRequest2);
        HttpRequest geocodingRequest = geocodingRequest2.timeout(Duration.ofMillis(2000)).build();

        HttpResponse geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        return (String) geocodingResponse.body();
    }
}
