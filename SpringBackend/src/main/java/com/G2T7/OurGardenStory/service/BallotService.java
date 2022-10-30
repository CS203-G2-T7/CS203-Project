package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Base64;
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
import com.G2T7.OurGardenStory.model.ReqResModel.UserSignInResponse;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.util.StringUtils;
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

    @Autowired
    private UserSignInResponse userSignInResponse;

    public Relationship findUsernameInBallot(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot == null) {
            throw new ResourceNotFoundException("Ballot does not exist.");
        } 
        return foundBallot;
    }

    public List<Relationship> addBallotInWindow(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);
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
            Relationship ballot = new Ballot(capWinId, username, payload.get("gardenName").asText(), "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance, "Pending");
            toAddRelationshipList.add(ballot);
        });
        
        dynamoDBMapper.batchSave(toAddRelationshipList);
        return toAddRelationshipList;
    }

    public Relationship updateGardenInBallot(String windowId, String username, JsonNode payload) throws Exception {
        String capWinId = StringUtils.capitalize(windowId);
        Relationship toUpdateBallot = findUsernameInBallot(capWinId, username);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String currentDate = date.format(formatter);

        toUpdateBallot = new Ballot(capWinId, username, payload.get("gardenName").asText(),  "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, toUpdateBallot.getDistance(), "Pending");

        dynamoDBMapper.save(toUpdateBallot);
        return toUpdateBallot;
    }      

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUsernameInBallot(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    public String getUsername() {
        String idToken = userSignInResponse.getIdToken();
        String[] chunks = idToken.split("\\."); // chunk 0 is header, chunk 1 is payload, chunk 2 is signature

        Base64.Decoder decoder = Base64.getUrlDecoder(); // Decode via Base64
        String decodedPayload = new String(decoder.decode(chunks[1])); // gets payload from idToken
        String[] payload_attr = decodedPayload.split(",");

        for (String payload : payload_attr) {
            if (payload.contains("username")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 1); // username is returned
            }
        }
        return null;
    }
}
