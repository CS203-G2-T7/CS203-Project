package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.repository.BallotRepo;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
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
    public Ballot saveBallot(@RequestBody JsonNode payload) {
        System.out.println("Ballot posted for " + payload.get("gardenName") + ".");
        return ballotRepo.save(payload.get("gardenName").asText());
    }

    @GetMapping(path = "/magic")
    public List<Ballot> callAlgo() {
        return ballotRepo.doMagic();
    }
}
