package com.G2T7.OurGardenStory.controller;

import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class HealthCheckController {

    @GetMapping(path = "/")
        public String healthCheckEndpoint() {
    return "Health Check";
    }

}
