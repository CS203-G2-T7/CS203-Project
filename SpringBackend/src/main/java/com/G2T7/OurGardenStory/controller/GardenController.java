package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.service.GardenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class GardenController {
    @Autowired
    private GardenService gardenService;

    // TODO: To implement
    @GetMapping(path = "/garden")
    public List<Garden> findGardenById(@RequestParam(name = "id") Optional<String> id) {
        return new ArrayList<Garden>();
    }

    // TODO: Check if garden already exists. Error handling.
    @PostMapping(path = "/garden")
    public Garden saveGarden(@RequestBody Garden garden) {
        return gardenService.createGarden(garden);
    }

    /*
     * TODO:
     * Garden update. PUT mapping.
     * Garden get. GET by gardenName(SK) and (PK) "Garden".
     * Garden get all by PK
     * 
     * 
     */
}