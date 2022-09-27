package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.repository.BallotRepo;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @Autowired
    private GardenRepo gardenRepo;

    @Autowired
    private BallotRepo ballotRepo;

    @PostMapping(path = "/home")
    public Garden saveGarden(@RequestBody Garden garden) {
        return gardenRepo.save(garden);
    }

    @GetMapping(path = "/home/{gardenId}")
    public Garden getGarden(@PathVariable("gardenId") String gardenId) {
        return gardenRepo.getGardenById(gardenId);
    }

    @PostMapping(path = "/ballot")
    public Ballot saveBallot(@RequestBody Ballot ballot) {
        return ballotRepo.save(ballot);
    }
}