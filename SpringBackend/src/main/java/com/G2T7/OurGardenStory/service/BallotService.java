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
import com.amazonaws.services.dynamodbv2.xspec.L;

import java.util.*;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;

import ch.qos.logback.core.net.SyslogOutputStream;

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

    public List<Relationship> findAllBallotsInWindowGarden(String windowId, String gardenName) {
        String capWinId = StringUtils.capitalize(windowId);
        String capWinIdGardenName = capWinId + "_" + gardenName;
 
        if (!relationshipService.validateWinExist(capWinId)) {
          throw new ResourceNotFoundException(capWinId + " does not exist.");
        }
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":GardenWinValue", new AttributeValue().withS(capWinIdGardenName));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
            .withIndexName("WinId_GardenName-index")
            .withConsistentRead(false)
            .withKeyConditionExpression("WinId_GardenName = :GardenWinValue")
            .withExpressionAttributeValues(eav);
    
        PaginatedQueryList<Relationship> foundRelationList = dynamoDBMapper.query(Relationship.class, qe);
        if (foundRelationList.isEmpty() || foundRelationList == null) {
          throw new ResourceNotFoundException("There are no ballots in " + capWinId + ".");
        }
        return foundRelationList;
      }

    public Relationship findUserBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        // if (foundBallot == null) {
        //     throw new ResourceNotFoundException("Ballot does not exist.");
        // }
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
        String currentDate = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        // validations
        // validateBallotPostDate(windowId, date);
        // validateGardenInWindow(windowId, payload.get("gardenName").asText());
        // validateUserHasBallotedBeforeInSameWindow(windowId, username);

        //String winId, String username, String gardenName, String ballotId, String ballotDateTime,
        //double distance, String ballotStatus
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance), currentDate, distance,
                "PENDING");

        dynamoDBMapper.save(ballot);
        System.out.println("Ballot is successfully posted");
        return ballot;
    }

    public Relationship updateGardenInBallot(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);
        Relationship toUpdateBallot = findUserBallotInWindow(capWinId, username);

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String currentDate = date.format(formatter);

        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        String latitude = garden.getLatitude();
        String longitude = garden.getLongitude();
        User user = userService.findUserByUsername(username);
        String userAddress = user.getAddress();
        double distance = geocodeService.saveDistance(username, userAddress, longitude, latitude);

        // Relationship ballot = new Ballot(capWinId, username, capWinId + "-" + garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
        //                         Relationship.BallotStatus.PENDING);
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
                                        "PENDING");

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    //Check ballot post date within window
    public void validateBallotPostDate(String windowId, LocalDate date) {
        Window win = windowService.findWindowById(windowId).get(0);
        String startDate = win.getSK();
        LocalDate winStartDate = convertStringToLocalDate(startDate);

        String winDuration = win.getWindowDuration();
        
        int winDurationMonth = Integer.parseInt(winDuration.substring(0, winDuration.indexOf('M')));
        LocalDate winEndDate = winStartDate.plusMonths(winDurationMonth); 

        if (date.isBefore(winStartDate) || date.isAfter(winEndDate)) {
            throw new IllegalArgumentException("Ballot is submitted outside of window balloting period");
        } 
    }

    public void validateGardenInWindow(String windowId, String gardenName) {
        relationshipService.findGardenInWindow(windowId, gardenName);
    }

    public void validateUserHasBallotedBeforeInSameWindow(String windowId, String username) {
        Relationship r = findUserBallotInWindow(windowId, username);
        if (r != null) {
            throw new IllegalArgumentException("User has already balloted in the same window");
        }
    }

    public LocalDate convertStringToLocalDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));

        LocalDate convertedDate = LocalDate.of(year, month, day);
        return convertedDate;
    }
}
