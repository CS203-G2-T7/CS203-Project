package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class GardenController {
    @Autowired
    private GardenRepo gardenRepo;

    @GetMapping(path = "/garden")
    public List<Garden> findGardenById(@RequestParam(name = "id") Optional<String> id) {
        if (id.isPresent()) {
            List<Garden> result = new ArrayList<Garden>();
            result.add(gardenRepo.findGardenById(id.get()));
            return result;
        } else {
            return gardenRepo.listGardens();
        }
    }

    @PostMapping(path = "/garden")
    public Garden saveGarden(@RequestBody Garden garden) {
        return gardenRepo.save(garden);
    }
}