package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private GardenRepo gardenRepo;

    @PostMapping(path = "/home")
    public Garden saveEmployee(@RequestBody Garden garden) {
        return gardenRepo.save(garden);
    }
}
