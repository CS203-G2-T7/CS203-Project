package com.G2T7.OurGardenStory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.service.CommunityService;

import com.fasterxml.jackson.databind.JsonNode;

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
     * @param payload which includes a String gardenName
     * @return the list of users with successful ballots for a particular garden
     */
    @GetMapping(path = "/community")
    public ResponseEntity<List<User>> findSuccessfulBallotsInGarden(@RequestBody JsonNode payload) {
        try {
            return ResponseEntity.ok(communityService.findUserWithSuccessfulBallotInGarden(payload));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
