package com.G2T7.OurGardenStory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.service.BallotService;
import com.G2T7.OurGardenStory.service.CommunityService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin("*")
@RestController
public class CommunityController {
    
    @Autowired
    private CommunityService communityService;

    @GetMapping(path = "/community")
    public ResponseEntity<List<User>> findSuccessfulbBallotsInGarden(@RequestBody JsonNode payload) {
        try {
            String gardenAddress = payload.get("gardenAddress").asText();
            
            return ResponseEntity.ok(communityService.findUserWithSucessfulBallotInGarden(gardenAddress));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
