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
        HttpClient httpClient = HttpClient.newHttpClient();

        String encodedQuery = URLEncoder.encode(query,"UTF-8");
        String requestUri = resource + "?apiKey=" + apiKey + "&q=" + encodedQuery;

        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();

        HttpResponse geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());

        return (String) geocodingResponse.body();
    }
}
