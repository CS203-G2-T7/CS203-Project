package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Plant;
import com.G2T7.OurGardenStory.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class HealthCheckController {

    @GetMapping(path = "/")
        public String healthCheckEndpoint() {
    return "Health Check";
    }

}
