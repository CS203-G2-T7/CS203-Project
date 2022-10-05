package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.BallotRepo;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {
    @Autowired
    private GardenRepo gardenRepo;

    @Autowired
    private BallotRepo ballotRepo;

    @Autowired
    private WindowRepo windowRepo;

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

    @PostMapping(path = "/window")
    public Window saveWindow(@RequestBody Window window) {
        return windowRepo.save(window);
    }

    @GetMapping(path = "/window")
    public List<Window> getWindows() {return  windowRepo.listWindows();}
}