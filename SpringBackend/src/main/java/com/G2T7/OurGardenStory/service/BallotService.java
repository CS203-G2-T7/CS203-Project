package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.controller.GeocodeService;
import com.G2T7.OurGardenStory.exception.CustomException;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.User;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Ballot;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.*;
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
    private GeocodeService geocodeService;

    @Autowired
    private WindowService windowService;

    @Autowired
    private RelationshipService relationshipService;

    public Relationship findUserBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot == null) {
            throw new ResourceNotFoundException("Ballot does not exist.");
        }
        return foundBallot;
    }

    public Relationship addBallotInWindow(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();

        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();

        double distance = geocodeService.saveDistance(username, userAddress, longitude, latitude);

        LocalDate date = LocalDate.now();
        String currentDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        // validations
        // validateUser(username);
        // validateBallotPostDate(windowId, date);
        // validateGardenInWindow(windowId, currentDate);
        // validateIfGardenFull(garden, capWinId);
        // validateUserHasBallotedBeforeInSameWindow(windowId, username);

        Relationship ballot = new Ballot(capWinId, username, capWinId + "|" + garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance,
                Relationship.BallotStatus.PENDING);
        // ballot.setPK(capWinId);
        // ballot.setSK(username);
        // ballot.setWinId_GardenName(windowId + "|" +
        // payload.get("gardenName").asText());
        // ballot.setBallotId("Ballot" + String.valueOf(++Ballot.numInstance));
        // ballot.setBallotDateTime(currentDate);
        // ballot.setDistance(distance);
        // ballot.setBallotStatus(Relationship.BallotStatus.PENDING);

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public Relationship updateGardenInBallot(String windowId, String username, JsonNode payload) throws Exception {
        String capWinId = StringUtils.capitalize(windowId);
        Relationship toUpdateBallot = findUserBallotInWindow(capWinId, username);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String currentDate = date.format(formatter);

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();
        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();
        double distance = geocodeService.saveDistance(username, userAddress, longitude, latitude);

        Ballot ballot = new Ballot();
        ballot.setPK(capWinId);
        ballot.setSK(username);
        ballot.setWinId_GardenName(windowId + "|" + payload.get("gardenName").asText());
        ballot.setBallotId(toUpdateBallot.getBallotId());
        ballot.setBallotDateTime(currentDate);
        ballot.setDistance(distance);
        ballot.setBallotStatus(Relationship.BallotStatus.PENDING);

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    public String getUsername() {
        // String idToken = UserSignInResponse.getIdToken();
        String idToken = "";
        String[] chunks = idToken.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decodedPayload = new String(decoder.decode(chunks[1]));
        String[] payload_attr = decodedPayload.split(",");

        for (String payload : payload_attr) {
            if (payload.contains("username")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 1); // username is returned
            }
        }
        return null;
    }

    public List<Relationship> findAllBallotsInWindowForGarden(String windowId, String gardenName) {
        String capWinId = StringUtils.capitalize(windowId);
        // if (!relationshipService.validateWinExist(capWinId)) {
        // throw new ResourceNotFoundException(capWinId + " does not exist.");
        // }
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":WINID", new AttributeValue().withS(capWinId));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                .withKeyConditionExpression("PK = :WINID").withExpressionAttributeValues(eav);

        PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
        if (foundRelationList.isEmpty() || foundRelationList == null) {
            throw new ResourceNotFoundException("There are no gardens in " + capWinId + ".");
        }
        return foundRelationList;
    }

    public void validateUser(String username) {
        User user = userService.findUserByUsername(username);
        String DOB = user.getDOB();
        LocalDate birthdate = LocalDate.of(Integer.parseInt(DOB.substring(6)),
                Integer.parseInt(DOB.substring(3, 5)), Integer.parseInt(DOB.substring(0, 2)));

        birthdate = birthdate.minusYears(18);
        if (birthdate.getYear() < 0) {
            throw new CustomException("User must be 18 to ballot");
        }
        return;
    }

    public void validateBallotPostDate(String windowId, LocalDate date) {
        // TODO
        Window win = windowService.findWindowById(windowId).get(0);
        // LocalDate startDate = win.getSK();
        return;
    }

    public void validateGardenInWindow(String windowId, String gardenName) {
        relationshipService.findGardenInWindow(windowId, gardenName);
    }

    public void validateIfGardenFull(Garden garden, String winId) {
        // TODO
        return;
    }

    public void validateUserHasBallotedBeforeInSameWindow(String windowId, String username) {
        Relationship r = findUserBallotInWindow(windowId, username);
        if (r != null) {
            throw new CustomException("User has already balloted in the same window");
        }
        return;
    }
}
