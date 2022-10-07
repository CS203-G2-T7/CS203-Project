package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.BallotRepo;
import com.G2T7.OurGardenStory.repository.GardenRepo;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping(path = "/ballots")
    public List<Ballot> getBallots() {
        return ballotRepo.listBallots();
    }

    @GetMapping(path = "/ballot")
    public List<Ballot> getBallot() {
        return ballotRepo.listBallotsFromLatestWindow();
    }

    @PostMapping(path = "/ballot")
    public Ballot saveBallot(@RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        return ballotRepo.save(payload.get("gardenName").asText());
    }

    @GetMapping(path = "/windows")
    public List<Window> getWindows() {return windowRepo.listWindows();}

    @GetMapping(path = "/window")
    public Window getLatestWindow() {return windowRepo.findLatestWindow();}

    @PostMapping(path = "/window")
    public Window saveWindow(@RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        Window window = new Window();
        window.setStartDateTime(LocalDateTime.parse(payload.get("startDateTime").asText()));
        window.setDuration(payload.get("duration").asText());
        String gardenName = payload.get("gardenName").asText();
        return windowRepo.save(window, gardenName);
    }

    @PutMapping(path = "/window")
    public Window updateWindow(@RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        Window window = new Window();
        window.setStartDateTime(LocalDateTime.parse(payload.get("startDateTime").asText()));
        String gardenName = payload.get("gardenName").asText();
        return windowRepo.update(window, gardenName);
    }
}