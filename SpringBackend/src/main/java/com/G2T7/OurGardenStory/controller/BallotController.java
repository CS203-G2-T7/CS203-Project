package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.repository.BallotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BallotController {
    @Autowired
    private BallotRepo ballotRepo;

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
}
