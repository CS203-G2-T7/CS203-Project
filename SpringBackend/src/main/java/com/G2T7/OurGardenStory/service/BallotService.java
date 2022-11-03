package com.G2T7.OurGardenStory.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.controller.GeocodeService;
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
    private WinGardenService relationshipService;

    /**
     * Finds all ballots given a window and a garden
     * Has to query GSI of WinId_GardenName-index to get ballots.
     * 
     * @windowId ID of window
     * @gardenName name of garden
     * @return List of found ballots
     * 
     *         Validations:
     *         1. Window exists
     *         2. Garden exists
     *         3. Garden is in window
     *         4. Ballots List not empty
     */

    public List<Relationship> findAllBallotsInWindowGarden(String windowId, String gardenName) {
        String capWinId = StringUtils.capitalize(windowId);

        if (!relationshipService.validateWinExist(capWinId)) {
            throw new ResourceNotFoundException(capWinId + " does not exist.");
        }
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":GardenWinValue", new AttributeValue().withS(capWinId + "_" + gardenName));
        DynamoDBQueryExpression<Relationship> qe = new DynamoDBQueryExpression<Relationship>()
                .withIndexName("WinId_GardenName-index")
                .withConsistentRead(false)
                .withKeyConditionExpression("WinId_GardenName = :GardenWinValue")
                .withExpressionAttributeValues(eav);

        PaginatedQueryList<Relationship> foundBallots = dynamoDBMapper.query(Relationship.class, qe);
        if (foundBallots.isEmpty() || foundBallots == null) {
            throw new ResourceNotFoundException("There are no ballots in " + capWinId + ".");
        }
        return foundBallots;
    }

    /**
     * Gets a user's ballot given windowId and username. Username supposedly from
     * JWT token. Ballots are unique by PK = windowId and SK = username. Can just
     * use load from DynamoDBMapper.
     * 
     * Validate:
     * 1. windowId exists
     * 
     * @param windowId
     * @param username
     * @return FoundBallot
     */

    public Relationship findUserBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot == null) {
            throw new ResourceNotFoundException("Ballot for " + username + " in window " + windowId + " not found");
        }
        return foundBallot;
    }

    /**
     * Adds ballot given garden, window and username. Called when place ballot
     * clicked
     * on frontend.
     * 
     * Basic validation:
     * 1. Window Exists
     * 2. Garden Exists
     * 3. Garden Window relationship exists
     * 4. User authenticated
     * 
     * Advanced validation:
     * 5. Ballot post date within window frame
     * 6. User has not balloted in same window before.
     * 
     * @param windowId
     * @param username
     * @param payload
     * @return addedBallot
     */

    public Relationship addBallotInWindowGarden(String windowId, String username, JsonNode payload) {
        String capWinId = StringUtils.capitalize(windowId);

        // Basic existence validations
        relationshipService.findGardenInWindow(capWinId, payload.get("gardenName").asText());
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        // Advanced validations
        validateBallotPostDate(capWinId, LocalDate.now());
        validateUserMultipleBallots(capWinId, username);

        // Passed all validations, then create call geocode. Create ballot and save.
        Garden garden = gardenService.findGardenByGardenName(payload.get("gardenName").asText());
        User user = userService.findUserByUsername(username);

        double distance = geocodeService.saveDistance(username, user.getAddress(), garden.getLongitude(),
                garden.getLatitude());

        Relationship ballot = new Ballot(capWinId, username, garden.getSK(),
                "Ballot" + String.valueOf(++Ballot.numInstance),
                LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")),
                distance, "PENDING");

        dynamoDBMapper.save(ballot);
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

        // Relationship ballot = new Ballot(capWinId, username, capWinId + "-" +
        // garden.getSK(), toUpdateBallot.getBallotId(), currentDate, distance,
        // Relationship.BallotStatus.PENDING);
        Relationship ballot = new Ballot(capWinId, username, garden.getSK(), toUpdateBallot.getBallotId(), currentDate,
                distance,
                "PENDING");

        dynamoDBMapper.save(ballot);
        return ballot;
    }

    public void deleteBallotInWindow(String windowId, String username) {
        String capWinId = StringUtils.capitalize(windowId);

        Relationship ballotToDelete = findUserBallotInWindow(capWinId, username);
        dynamoDBMapper.delete(ballotToDelete);
    }

    // Check ballot post date within window
    public void validateBallotPostDate(String capWinId, LocalDate date) {
        Window foundWindow = windowService.findWindowById(capWinId).get(0);
        LocalDate winStartDate = convertStringToLocalDate(foundWindow.getSK());

        String winDurationString = foundWindow.getWindowDuration(); // In Period format. 3M, 1d, 1Y
        Period winDuration = convertStringToPeriod(winDurationString); // P3M -> 3 months

        LocalDate winEndDate = winStartDate.plus(winDuration);

        String errorMessage = null;
        if (date.isBefore(winStartDate)) {
            errorMessage = "before";
        } else if (date.isAfter(winEndDate)) {
            errorMessage = "after";
        }

        if (errorMessage != null) {
            throw new IllegalArgumentException(
                    "Ballot is submitted " + errorMessage + " " + capWinId
                            + " balloting period.\nStart date: "
                            + winStartDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")) + "\nEnd date:   "
                            + winEndDate.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
        }
        // reach here no error
    }

    public void validateUserMultipleBallots(String capWinId, String username) {
        Relationship foundBallot = dynamoDBMapper.load(Ballot.class, capWinId, username);
        if (foundBallot != null) {
            throw new IllegalArgumentException("User " + username + " has already balloted in window " + capWinId);
        }
    }

    /**
     * Converts string in the format MM-dd-yyyy to a LocalDate object.
     * 
     * @param date
     * @return convertedLocalDate object
     */
    public LocalDate convertStringToLocalDate(String date) {
        String dateComponents[] = date.split("-");

        int year = Integer.parseInt(dateComponents[2]);
        int month = Integer.parseInt(dateComponents[0]);
        int day = Integer.parseInt(dateComponents[1]);

        LocalDate convertedDate = LocalDate.of(year, month, day);
        return convertedDate;
    }

    // Obtains a Period from a text string such as PnYnMnD
    public Period convertStringToPeriod(String periodInput) {
        Period convertedPeriod = Period.parse(periodInput);
        return convertedPeriod;
    }
}
