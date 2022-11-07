package com.G2T7.OurGardenStory.controller;

import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;


@CrossOrigin("https://ourgardenstory.me")
@RestController
@ApiIgnore
public class HealthCheckController {

    @GetMapping(path = "/")
        public String healthCheckEndpoint() {
    return "Health Check";
    }

}
