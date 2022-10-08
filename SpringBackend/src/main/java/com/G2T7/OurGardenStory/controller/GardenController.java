package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GardenController {
    @Autowired
    private GardenRepo gardenRepo;

    @GetMapping(path = "/garden")
    public List<Garden> getGardens() {
        return gardenRepo.listGardens();
    }

    @PostMapping(path = "/garden")
    public Garden saveGarden(@RequestBody Garden garden) {
        return gardenRepo.save(garden);
    }
}