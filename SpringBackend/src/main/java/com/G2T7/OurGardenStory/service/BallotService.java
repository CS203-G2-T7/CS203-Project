package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Relation;
import javax.print.attribute.standard.MediaSize.ISO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.controller.GeocodeController;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BallotService {
    
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private GardenService gardenService;

    @Autowired
    private UserService userService;

    @Autowired
    private GeocodeController geocodeController;

    public Relationship findUsernameInBallot(String winId, String username) {
        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, winId, username);
        if (foundBallot == null) {
            throw new ResourceNotFoundException("Ballot does not exist.");
        } 
        return foundBallot;
    }

    public List<Relationship> addBallot(String username, JsonNode payload) {
        List<Relationship> toAddRelationshipList = new ArrayList<Relationship>();

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();

        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();

        double distance = geocodeController.saveDistance(username, userAddress, longitude, latitude);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String currentDate = date.format(formatter);

        payload.forEach(relation -> {
            Relationship ballot = new Ballot(payload.get("winId").asText(), username, payload.get("gardenName").asText(), "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance, "Pending");
            toAddRelationshipList.add(ballot);
        });
        
        dynamoDBMapper.batchSave(toAddRelationshipList);
        return toAddRelationshipList;
    }

    public Relationship updateGardenInBallot(String winId, String username, JsonNode payload) throws Exception {
        Relationship toUpdateBallot = findUsernameInBallot(winId, username);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String currentDate = date.format(formatter);

        toUpdateBallot = new Ballot(winId, username, payload.get("gardenName").asText(),  "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, toUpdateBallot.getDistance(), "Pending");

        dynamoDBMapper.save(toUpdateBallot);
        return toUpdateBallot;
    }      

    public void deleteBallotInWindow(String winId, String username) {
        Relationship ballotToDelete = findUsernameInBallot(winId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }
}
