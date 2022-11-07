package com.G2T7.OurGardenStory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.service.CommunityService;

@CrossOrigin("*")
@RestController
public class CommunityController {

    private CommunityService communityService;

    @Autowired public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    /**
     * Gets a list of all users with successful ballots for a particular garden
     * If the garden is not found, throw an exception
     *
     * @param gardenName a String
     * @return the list of users with successful ballots for a particular garden
     */
    @GetMapping(path = "/community/{gardenName}")
    public ResponseEntity<List<User>> findSuccessfulBallotsInGarden(@PathVariable String gardenName) {
        try {
            return ResponseEntity.ok(communityService.findUserWithSuccessfulBallotInGarden(gardenName));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
